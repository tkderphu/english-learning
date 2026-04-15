package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Nhật ký hoạt động học (flashcard, book, exercise, chat AI, …) phục vụ thống kê & heatmap.
 */
@Entity
@Table(name = "learning_activities")
@Getter
@Setter
public class LearningActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** Giá trị {@link site.viosmash.english.domain.LearningActivityType#name()}. */
    @Column(name = "activity_type", nullable = false, length = 32)
    private String activityType;

    @Column(length = 512)
    private String title;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "score_percent", precision = 5, scale = 2)
    private BigDecimal scorePercent;

    @Column(name = "words_new_count")
    private Integer wordsNewCount;

    @Lob
    @Column(name = "detail_json", columnDefinition = "TEXT")
    private String detailJson;

    @Column(name = "reference_type", length = 64)
    private String referenceType;

    @Column(name = "reference_id")
    private Integer referenceId;
}
