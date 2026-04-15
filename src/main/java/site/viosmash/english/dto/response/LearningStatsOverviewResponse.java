package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningStatsOverviewResponse {
    /** Số phiên sách / bài tập đã ghi nhận (BOOK + EXERCISE). */
    private long completedLessonsOrExercises;
    /** Số từ trong danh sách từ vựng đã học. */
    private long wordsLearnedCount;
    /** Chuỗi ngày học liên tiếp hiện tại. */
    private int currentStreakDays;
    /** Tổng số ngày (không trùng) đã có hoạt động. */
    private long totalStudyDays;
}
