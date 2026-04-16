package site.viosmash.english.domain;

/**
 * Loại hoạt động ghi nhận trong lịch sử học (profile / heatmap / chi tiết ngày).
 */
public enum LearningActivityType {
    FLASHCARD,
    /** Reading / study sessions tied to a {@code BOOK} reference. */
    BOOK,
    EXERCISE,
    AI_CHAT,
    OTHER
}
