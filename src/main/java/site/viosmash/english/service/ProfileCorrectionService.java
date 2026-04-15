package site.viosmash.english.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.viosmash.english.dto.response.CorrectionSessionReviewResponse;
import site.viosmash.english.dto.response.SessionCorrectionDetailResponse;
import site.viosmash.english.dto.response.UserCorrectionItemResponse;
import site.viosmash.english.entity.AiChatSession;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.AiChatSessionRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Lịch sử lỗi ngôn ngữ chỉ từ <strong>AI Chat</strong>: bảng {@code ai_message_errors}
 * gắn với tin nhắn trong {@code ai_chat_sessions} (phiên SCENARIO / FREE_CHAT).
 */
@Service
@RequiredArgsConstructor
public class ProfileCorrectionService {

    /** Chỉ phiên AI Chat do app tạo ({@code SCENARIO} / {@code FREE_CHAT}); {@code NULL} = dữ liệu cũ. */
    private static final String AI_CHAT_SESSION_TYPE_FILTER =
            " AND (s.session_type IS NULL OR UPPER(TRIM(s.session_type)) IN ('SCENARIO', 'FREE_CHAT'))";

    private final EntityManager entityManager;
    private final AiChatSessionRepository chatSessionRepository;

    @Transactional(readOnly = true)
    public Page<UserCorrectionItemResponse> listCorrections(int userId, String filter, String q, Pageable pageable) {
        String typePart = typeClause(filter);
        boolean hasSearch = q != null && !q.isBlank();
        String searchPart = hasSearch
                ? " AND (LOWER(e.original_text) LIKE :search OR LOWER(e.suggested_text) LIKE :search "
                + "OR LOWER(COALESCE(s.title,'')) LIKE :search OR LOWER(COALESCE(e.explanation,'')) LIKE :search) "
                : "";

        String baseSql = """
                FROM ai_message_errors e
                INNER JOIN ai_message_feedback f ON e.feedback_id = f.id
                INNER JOIN ai_chat_messages m ON f.message_id = m.id
                INNER JOIN ai_chat_sessions s ON m.session_id = s.id
                WHERE s.user_id = :userId
                """
                + AI_CHAT_SESSION_TYPE_FILTER
                + typePart
                + searchPart;

        Query countQ = entityManager.createNativeQuery("SELECT COUNT(e.id) " + baseSql);
        countQ.setParameter("userId", userId);
        if (hasSearch) {
            countQ.setParameter("search", "%" + q.trim().toLowerCase() + "%");
        }
        long total = ((Number) countQ.getSingleResult()).longValue();

        String dataSql = """
                SELECT e.id, e.error_type, e.original_text, e.suggested_text, e.explanation,
                f.created_at, m.id, m.session_id, s.title, s.session_type
                """
                + baseSql
                + " ORDER BY f.created_at DESC";

        Query dataQ = entityManager.createNativeQuery(dataSql);
        dataQ.setParameter("userId", userId);
        if (hasSearch) {
            dataQ.setParameter("search", "%" + q.trim().toLowerCase() + "%");
        }
        dataQ.setFirstResult((int) pageable.getOffset());
        dataQ.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<Object[]> rows = dataQ.getResultList();
        List<UserCorrectionItemResponse> items = new ArrayList<>();
        for (Object[] row : rows) {
            items.add(mapListRow(row));
        }
        return new PageImpl<>(items, pageable, total);
    }

    @Transactional(readOnly = true)
    public CorrectionSessionReviewResponse sessionReview(int userId, int sessionId) {
        AiChatSession session = chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Session not found"));

        String sql = """
                SELECT e.id, e.error_type, e.original_text, e.suggested_text, e.explanation,
                       f.created_at, m.id, m.created_at
                FROM ai_message_errors e
                INNER JOIN ai_message_feedback f ON e.feedback_id = f.id
                INNER JOIN ai_chat_messages m ON f.message_id = m.id
                INNER JOIN ai_chat_sessions s ON m.session_id = s.id
                WHERE s.user_id = :userId AND s.id = :sessionId
                """
                + AI_CHAT_SESSION_TYPE_FILTER
                + """
                 ORDER BY m.created_at ASC, e.id ASC
                """;

        Query q = entityManager.createNativeQuery(sql);
        q.setParameter("userId", userId);
        q.setParameter("sessionId", sessionId);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();

        List<SessionCorrectionDetailResponse> improvements = new ArrayList<>();
        for (Object[] row : rows) {
            improvements.add(SessionCorrectionDetailResponse.builder()
                    .errorId(((Number) row[0]).intValue())
                    .category(formatCategory((String) row[1]))
                    .originalText((String) row[2])
                    .suggestedText((String) row[3])
                    .explanation((String) row[4])
                    .build());
        }

        LocalDateTime started = session.getStartedAt();
        LocalDateTime ended = session.getEndedAt() != null ? session.getEndedAt() : session.getLastMessageAt();
        int durationMin = 0;
        if (started != null && ended != null) {
            durationMin = (int) Math.max(1, ChronoUnit.MINUTES.between(started, ended));
        }

        String title = session.getTitle() != null ? session.getTitle() : "AI session";
        return CorrectionSessionReviewResponse.builder()
                .sessionId(session.getId())
                .contextHeader("CONVERSATION WITH AI TUTOR")
                .sessionTitle(title)
                .sessionStartedAt(started)
                .sessionEndedAt(ended)
                .durationMinutes(durationMin)
                .improvementCount(improvements.size())
                .improvements(improvements)
                .build();
    }

    @Transactional
    public void clearHistory(int userId) {
        String sql = """
                DELETE e FROM ai_message_errors e
                INNER JOIN ai_message_feedback f ON e.feedback_id = f.id
                INNER JOIN ai_chat_messages m ON f.message_id = m.id
                INNER JOIN ai_chat_sessions s ON m.session_id = s.id
                WHERE s.user_id = :userId
                """
                + AI_CHAT_SESSION_TYPE_FILTER;
        Query q = entityManager.createNativeQuery(sql);
        q.setParameter("userId", userId);
        q.executeUpdate();
    }

    private static String typeClause(String filter) {
        if (filter == null) {
            return "";
        }
        return switch (filter.trim().toUpperCase()) {
            case "GRAMMAR" -> " AND LOWER(e.error_type) LIKE '%grammar%' ";
            case "VOCABULARY" -> " AND (LOWER(e.error_type) LIKE '%vocab%' OR LOWER(e.error_type) LIKE '%word%') ";
            case "SPELLING" -> " AND (LOWER(e.error_type) LIKE '%spell%' OR LOWER(e.error_type) LIKE '%orthograph%') ";
            default -> "";
        };
    }

    private static UserCorrectionItemResponse mapListRow(Object[] row) {
        LocalDateTime occurred = toLdt(row[5]);
        String sessionType = (String) row[9];
        String sessionTitle = (String) row[8];
        return UserCorrectionItemResponse.builder()
                .errorId(((Number) row[0]).intValue())
                .errorType((String) row[1])
                .originalText((String) row[2])
                .suggestedText((String) row[3])
                .explanation((String) row[4])
                .occurredAt(occurred)
                .messageId(((Number) row[6]).intValue())
                .sessionId(((Number) row[7]).intValue())
                .sessionTitle(sessionTitle)
                .sourceLabel(buildSourceLabel(sessionType, sessionTitle))
                .build();
    }

    private static LocalDateTime toLdt(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        }
        if (v instanceof LocalDateTime ldt) {
            return ldt;
        }
        return null;
    }

    /** Luôn mô tả nguồn là phiên AI Chat (không còn nhãn lesson/book). */
    private static String buildSourceLabel(String sessionType, String title) {
        String t = title != null ? title.trim() : "";
        if (sessionType != null) {
            String u = sessionType.trim().toUpperCase();
            if ("SCENARIO".equals(u) && !t.isEmpty()) {
                return "Chat: " + t;
            }
            if ("FREE_CHAT".equals(u)) {
                return t.isEmpty() ? "Daily Conversation Chat" : "Chat: " + t;
            }
        }
        if (t.isEmpty()) {
            return "AI Chat";
        }
        return "Chat: " + t;
    }

    private static String formatCategory(String errorType) {
        if (errorType == null || errorType.isBlank()) {
            return "GRAMMAR";
        }
        return errorType.trim().toUpperCase().replace(' ', '_');
    }
}
