package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Từ vựng người học đã ghi nhận (flashcard / bài học / nhập tay).
 */
@Entity
@Table(
        name = "user_learned_words",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_term", columnNames = {"user_id", "term"})
)
@Getter
@Setter
public class UserLearnedWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false, length = 255)
    private String term;

    @Column(length = 128)
    private String phonetic;

    @Lob
    @Column(name = "definition", columnDefinition = "TEXT")
    private String definition;

    @Column(name = "source_module", length = 64)
    private String sourceModule;

    @Column(name = "is_favorite")
    private Boolean favorite;

    @Column(name = "needs_attention")
    private Boolean needsAttention;

    @Column(name = "audio_url", length = 1024)
    private String audioUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
