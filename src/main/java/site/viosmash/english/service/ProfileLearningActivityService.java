package site.viosmash.english.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.viosmash.english.domain.LearningActivityType;
import site.viosmash.english.dto.request.LogLearningActivityRequest;
import site.viosmash.english.dto.response.ActivityDayDetailResponse;
import site.viosmash.english.dto.response.HeatmapDayResponse;
import site.viosmash.english.dto.response.LearningActivityItemResponse;
import site.viosmash.english.dto.response.LearningStatsOverviewResponse;
import site.viosmash.english.entity.AiChatSession;
import site.viosmash.english.entity.LearningActivity;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.profile.StreakCalculator;
import site.viosmash.english.repository.LearningActivityRepository;
import site.viosmash.english.repository.UserLearnedWordRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileLearningActivityService {

    private static final int HEATMAP_LOW_MAX_MIN = 14;
    private static final int HEATMAP_MED_MAX_MIN = 44;

    private final LearningActivityRepository learningActivityRepository;
    private final UserLearnedWordRepository userLearnedWordRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public LearningActivityItemResponse logActivity(Integer userId, LogLearningActivityRequest req) {
        LearningActivityType type;
        try {
            type = LearningActivityType.valueOf(req.getActivityType().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Invalid activityType: " + req.getActivityType());
        }

        LearningActivity a = new LearningActivity();
        a.setUserId(userId);
        a.setActivityType(type.name());
        a.setTitle(req.getTitle());
        a.setStartedAt(req.getStartedAt());
        a.setEndedAt(req.getEndedAt());
        a.setScorePercent(req.getScorePercent());
        a.setWordsNewCount(req.getWordsNewCount());
        a.setReferenceType(req.getReferenceType());
        a.setReferenceId(req.getReferenceId());

        int duration = resolveDurationSeconds(req);
        a.setDurationSeconds(duration);

        if (req.getDetail() != null && !req.getDetail().isEmpty()) {
            try {
                a.setDetailJson(objectMapper.writeValueAsString(req.getDetail()));
            } catch (JsonProcessingException e) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "detail must be JSON-serializable");
            }
        }

        LearningActivity saved = learningActivityRepository.save(a);
        return toItem(saved);
    }

    private static int resolveDurationSeconds(LogLearningActivityRequest req) {
        if (req.getDurationSeconds() != null && req.getDurationSeconds() > 0) {
            return req.getDurationSeconds();
        }
        if (req.getStartedAt() != null && req.getEndedAt() != null) {
            long sec = java.time.Duration.between(req.getStartedAt(), req.getEndedAt()).getSeconds();
            if (sec > 0) {
                return (int) Math.min(sec, Integer.MAX_VALUE);
            }
        }
        throw new ServiceException(HttpStatus.BAD_REQUEST, "Provide durationSeconds or both startedAt and endedAt");
    }

    public LearningStatsOverviewResponse overview(Integer userId) {
        long lessons = learningActivityRepository.countByUserIdAndActivityTypeIn(
                userId,
                List.of(LearningActivityType.LESSON.name(), LearningActivityType.EXERCISE.name())
        );
        long words = userLearnedWordRepository.countByUserId(userId);
        long totalDays = learningActivityRepository.countDistinctStudyDaysNative(userId);

        List<java.sql.Date> dates = learningActivityRepository.findDistinctActivityDates(userId);
        Set<LocalDate> daySet = dates.stream().map(Date::toLocalDate).collect(Collectors.toSet());
        int streak = StreakCalculator.currentStreak(daySet, LocalDate.now());

        return LearningStatsOverviewResponse.builder()
                .completedLessonsOrExercises(lessons)
                .wordsLearnedCount(words)
                .currentStreakDays(streak)
                .totalStudyDays(totalDays)
                .build();
    }

    public List<HeatmapDayResponse> heatmap(Integer userId, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            to = LocalDate.now();
            from = to.minusDays(364);
        }
        if (to.isBefore(from)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "to must be >= from");
        }

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay();

        List<Object[]> rows = learningActivityRepository.aggregateMinutesByDay(userId, start, end);
        Map<LocalDate, long[]> agg = new HashMap<>();
        for (Object[] row : rows) {
            LocalDate d = ((Date) row[0]).toLocalDate();
            long totalSec = ((Number) row[1]).longValue();
            long cnt = ((Number) row[2]).longValue();
            agg.put(d, new long[]{totalSec, cnt});
        }

        List<HeatmapDayResponse> out = new ArrayList<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            long[] v = agg.getOrDefault(d, new long[]{0, 0});
            int minutes = (int) (v[0] / 60);
            int intensity = intensityFromMinutes(minutes);
            out.add(HeatmapDayResponse.builder()
                    .date(d)
                    .intensity(intensity)
                    .totalMinutes(minutes)
                    .activityCount(v[1])
                    .build());
        }
        return out;
    }

    private static int intensityFromMinutes(int minutes) {
        if (minutes <= 0) {
            return 0;
        }
        if (minutes <= HEATMAP_LOW_MAX_MIN) {
            return 1;
        }
        if (minutes <= HEATMAP_MED_MAX_MIN) {
            return 2;
        }
        return 3;
    }

    public ActivityDayDetailResponse dayDetail(Integer userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        List<LearningActivity> list = learningActivityRepository
                .findByUserIdAndStartedAtBetweenOrderByStartedAtAsc(userId, start, end);

        long totalSec = list.stream()
                .map(LearningActivity::getDurationSeconds)
                .filter(s -> s != null && s > 0)
                .mapToInt(Integer::intValue)
                .sum();

        List<BigDecimal> scores = list.stream()
                .map(LearningActivity::getScorePercent)
                .filter(s -> s != null)
                .toList();
        BigDecimal avg = null;
        if (!scores.isEmpty()) {
            BigDecimal sum = scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            avg = sum.divide(BigDecimal.valueOf(scores.size()), 2, RoundingMode.HALF_UP);
        }

        String insight = buildInsight(totalSec / 60, list.size(), avg);

        return ActivityDayDetailResponse.builder()
                .date(date)
                .totalMinutes((int) (totalSec / 60))
                .activityCount(list.size())
                .averageScorePercent(avg)
                .dailyInsight(insight)
                .activities(list.stream().map(ProfileLearningActivityService::toItem).toList())
                .build();
    }

    private static String buildInsight(long totalMinutes, long activityCount, BigDecimal avgScore) {
        if (activityCount == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("You studied ").append(totalMinutes).append(" minute(s) across ").append(activityCount).append(" activit").append(activityCount == 1 ? "y" : "ies").append(".");
        if (avgScore != null) {
            sb.append(" Average score: ").append(avgScore).append("%.");
        }
        return sb.toString();
    }

    private static LearningActivityItemResponse toItem(LearningActivity a) {
        return LearningActivityItemResponse.builder()
                .id(a.getId())
                .activityType(a.getActivityType())
                .title(a.getTitle())
                .startedAt(a.getStartedAt())
                .endedAt(a.getEndedAt())
                .durationSeconds(a.getDurationSeconds())
                .scorePercent(a.getScorePercent())
                .wordsNewCount(a.getWordsNewCount())
                .detailJson(a.getDetailJson())
                .referenceType(a.getReferenceType())
                .referenceId(a.getReferenceId())
                .build();
    }

    /**
     * Ghi nhận hoàn thành phiên AI Chat vào lịch sử học (heatmap / ngày). Idempotent theo session.
     */
    @Transactional
    public void logAiChatSessionEnded(Integer userId, AiChatSession session, int durationSeconds) {
        if (userId == null || session == null || session.getId() == null) {
            return;
        }
        if (learningActivityRepository.existsByUserIdAndReferenceTypeAndReferenceId(userId, "AI_CHAT_SESSION", session.getId())) {
            return;
        }
        LearningActivity a = new LearningActivity();
        a.setUserId(userId);
        a.setActivityType(LearningActivityType.AI_CHAT.name());
        a.setTitle(session.getTitle() != null ? session.getTitle() : "AI Chat");
        LocalDateTime start = session.getStartedAt() != null
                ? session.getStartedAt()
                : (session.getEndedAt() != null
                ? session.getEndedAt().minusSeconds(Math.max(1, durationSeconds))
                : LocalDateTime.now().minusSeconds(Math.max(1, durationSeconds)));
        a.setStartedAt(start);
        a.setEndedAt(session.getEndedAt() != null ? session.getEndedAt() : LocalDateTime.now());
        a.setDurationSeconds(Math.max(0, durationSeconds));
        a.setReferenceType("AI_CHAT_SESSION");
        a.setReferenceId(session.getId());
        Map<String, Object> detail = new HashMap<>();
        detail.put("sessionId", session.getId());
        detail.put("sessionType", session.getSessionType());
        detail.put("scenarioTitle", session.getTitle());
        try {
            a.setDetailJson(objectMapper.writeValueAsString(detail));
        } catch (JsonProcessingException e) {
            a.setDetailJson(null);
        }
        learningActivityRepository.save(a);
    }

    /**
     * Ghi nhận hoàn thành một phiên học flashcard (deck). Không idempotent — mỗi lần gọi là một bản ghi.
     */
    @Transactional
    public void logFlashcardDeckStudyCompleted(
            Integer userId,
            Integer deckId,
            String deckTitle,
            int durationSeconds,
            Integer cardsReviewed,
            Integer quizCorrect,
            Integer quizTotal
    ) {
        if (userId == null || deckId == null) {
            return;
        }
        int dur = Math.max(0, durationSeconds);
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusSeconds(Math.max(1, dur));

        LearningActivity a = new LearningActivity();
        a.setUserId(userId);
        a.setActivityType(LearningActivityType.FLASHCARD.name());
        a.setTitle(deckTitle != null && !deckTitle.isBlank() ? deckTitle : "Flashcard deck");
        a.setStartedAt(start);
        a.setEndedAt(end);
        a.setDurationSeconds(dur);
        a.setReferenceType("DECK");
        a.setReferenceId(deckId);

        if (quizCorrect != null && quizTotal != null && quizTotal > 0) {
            BigDecimal score = BigDecimal.valueOf(quizCorrect * 100.0 / quizTotal)
                    .setScale(2, RoundingMode.HALF_UP);
            a.setScorePercent(score);
        }

        Map<String, Object> detail = new HashMap<>();
        detail.put("deckId", deckId);
        if (cardsReviewed != null) {
            detail.put("cardsReviewed", cardsReviewed);
        }
        if (quizCorrect != null) {
            detail.put("quizCorrect", quizCorrect);
        }
        if (quizTotal != null) {
            detail.put("quizTotal", quizTotal);
        }
        try {
            a.setDetailJson(objectMapper.writeValueAsString(detail));
        } catch (JsonProcessingException e) {
            a.setDetailJson(null);
        }

        learningActivityRepository.save(a);
    }

    /**
     * Ghi nhận một phiên đọc sách (lesson) cho heatmap / lịch sử.
     */
    @Transactional
    public void logBookReadingSession(
            Integer userId,
            Integer bookId,
            String bookTitle,
            int durationSeconds,
            double progressPercent,
            int lastReadPageNumber
    ) {
        if (userId == null || bookId == null) {
            return;
        }
        int dur = Math.max(0, durationSeconds);
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusSeconds(Math.max(1, dur));

        LearningActivity a = new LearningActivity();
        a.setUserId(userId);
        a.setActivityType(LearningActivityType.LESSON.name());
        a.setTitle(bookTitle != null && !bookTitle.isBlank() ? bookTitle : "Reading");
        a.setStartedAt(start);
        a.setEndedAt(end);
        a.setDurationSeconds(dur);
        a.setReferenceType("BOOK");
        a.setReferenceId(bookId);

        Map<String, Object> detail = new HashMap<>();
        detail.put("bookId", bookId);
        detail.put("progressPercent", progressPercent);
        detail.put("lastReadPageNumber", lastReadPageNumber);
        try {
            a.setDetailJson(objectMapper.writeValueAsString(detail));
        } catch (JsonProcessingException e) {
            a.setDetailJson(null);
        }

        learningActivityRepository.save(a);
    }

    /**
     * Lịch sử bài học / bài tập đã ghi nhận (LESSON, EXERCISE), phân trang.
     *
     * @param filter ALL | LESSON | EXERCISE (không phân biệt hoa thường)
     */
    @Transactional(readOnly = true)
    public Page<LearningActivityItemResponse> activityHistory(Integer userId, String filter, String q, Pageable pageable) {
        List<String> types = typesForHistoryFilter(filter);
        String query = (q == null || q.isBlank()) ? null : q.trim();
        return learningActivityRepository.findHistory(userId, types, query, pageable)
                .map(ProfileLearningActivityService::toItem);
    }

    private static List<String> typesForHistoryFilter(String filter) {
        String f = filter == null ? "ALL" : filter.trim().toUpperCase();
        return switch (f) {
            case "EXERCISE" -> List.of(LearningActivityType.EXERCISE.name());
            case "LESSON" -> List.of(LearningActivityType.LESSON.name());
            default -> List.of(LearningActivityType.EXERCISE.name(), LearningActivityType.LESSON.name());
        };
    }
}
