package site.viosmash.english.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_message_feedback")
@Getter
@Setter
public class AiMessageFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "pronunciation_score")
    private BigDecimal pronunciationScore;

    @Column(name = "grammar_score")
    private BigDecimal grammarScore;

    @Column(name = "vocabulary_score")
    private BigDecimal vocabularyScore;

    @Column(name = "fluency_score")
    private BigDecimal fluencyScore;

    @Column(name = "overall_comment", columnDefinition = "TEXT")
    private String overallComment;

    @Column(name = "improved_version", columnDefinition = "TEXT")
    private String improvedVersion;

    @Column(name = "natural_suggestion", columnDefinition = "TEXT")
    private String naturalSuggestion;

    @Column(name = "error_count")
    private Integer errorCount;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
