package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.response.LearningMemoryResponse;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LearningMemoryService {

    private final NamedParameterJdbcTemplate jdbc;

    public LearningMemoryResponse buildMemory(Integer userId) {
        Map<String, Object> params = Map.of("userId", userId);
        List<String> recurringErrors = jdbc.query(
                """
                SELECT CONCAT(
                    UPPER(TRIM(COALESCE(e.error_type, 'UNKNOWN'))),
                    ': ',
                    COALESCE(NULLIF(TRIM(e.original_text), ''), 'recent mistake')
                ) AS recent_error
                FROM ai_message_errors e
                INNER JOIN ai_message_feedback f ON e.feedback_id = f.id
                INNER JOIN ai_chat_messages m ON f.message_id = m.id
                INNER JOIN ai_chat_sessions s ON m.session_id = s.id
                WHERE s.user_id = :userId
                ORDER BY m.created_at DESC
                LIMIT 5
                """,
                params,
                (rs, i) -> rs.getString("recent_error")
        );
        List<String> weakSkills = jdbc.query(
                """
                SELECT CONCAT('From recent session: ', skill) AS weak_skill
                FROM (
                    SELECT CASE
                        WHEN UPPER(COALESCE(grammar_level,'')) = 'NEED_IMPROVEMENT' THEN 'GRAMMAR'
                        WHEN UPPER(COALESCE(vocabulary_level,'')) = 'NEED_IMPROVEMENT' THEN 'VOCABULARY'
                        WHEN UPPER(COALESCE(fluency_level,'')) = 'NEED_IMPROVEMENT' THEN 'FLUENCY'
                        ELSE 'GENERAL'
                    END AS skill,
                    sm.created_at
                    FROM ai_session_summaries sm
                    INNER JOIN ai_chat_sessions s ON sm.session_id = s.id
                    WHERE s.user_id = :userId
                    ORDER BY sm.created_at DESC
                    LIMIT 2
                ) t
                WHERE skill <> 'GENERAL'
                """,
                params,
                (rs, i) -> rs.getString("weak_skill")
        );
        List<String> preferredTopics = jdbc.query(
                """
                SELECT COALESCE(NULLIF(TRIM(title), ''), 'General conversation') AS label
                FROM ai_chat_sessions
                WHERE user_id = :userId
                ORDER BY created_at DESC
                LIMIT 3
                """,
                params,
                (rs, i) -> rs.getString("label")
        );
        List<String> lastGoals = jdbc.query(
                """
                SELECT COALESCE(NULLIF(TRIM(instruction_snapshot), ''), 'General speaking')
                FROM ai_chat_sessions
                WHERE user_id = :userId
                ORDER BY created_at DESC
                LIMIT 3
                """,
                params,
                (rs, i) -> rs.getString(1)
        );
        List<String> mastered = recurringErrors.isEmpty() ? List.of("CONSISTENCY") : List.of();
        String confidence = weakSkills.isEmpty() ? "HIGH" : (weakSkills.size() == 1 ? "MEDIUM" : "BUILDING");
        return LearningMemoryResponse.builder()
                .recurringErrors(recurringErrors)
                .weakSkills(weakSkills)
                .masteredPatterns(mastered)
                .preferredTopics(preferredTopics)
                .confidenceLevel(confidence)
                .last3SessionGoals(lastGoals)
                .build();
    }

    public String toPromptBlock(Integer userId, String goalType, String focusSkill, String coachingMode) {
        LearningMemoryResponse m = buildMemory(userId);
        return """
                Learner memory (cross-session coaching):
                - Recurring errors: %s
                - Weak skills: %s
                - Session goal: %s
                - Focus skill: %s
                - Mode: %s
                - Preferred topics: %s
                - Confidence level: %s
                - Recent goals: %s
                Use this memory subtly while keeping current turn natural.
                """.formatted(
                String.join(", ", m.getRecurringErrors()),
                String.join(", ", m.getWeakSkills()),
                safe(goalType),
                safe(focusSkill),
                safe(coachingMode),
                String.join(", ", m.getPreferredTopics()),
                m.getConfidenceLevel(),
                String.join(", ", m.getLast3SessionGoals())
        );
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "not provided" : s.trim();
    }
}
