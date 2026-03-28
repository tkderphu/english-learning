package site.viosmash.english.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_session_summaries")
@Getter
@Setter
public class AiSessionSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "fluency_level")
    private String fluencyLevel;

    @Column(name = "grammar_level")
    private String grammarLevel;

    @Column(name = "vocabulary_level")
    private String vocabularyLevel;

    @Column(name = "sentence_count")
    private Integer sentenceCount;

    @Column(name = "error_count")
    private Integer errorCount;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "next_suggestion", columnDefinition = "TEXT")
    private String nextSuggestion;

    @Column(name = "related_topics", columnDefinition = "TEXT")
    private String relatedTopics;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
