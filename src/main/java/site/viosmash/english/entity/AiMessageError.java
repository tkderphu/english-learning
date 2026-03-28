package site.viosmash.english.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ai_message_errors")
@Getter
@Setter
public class AiMessageError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "feedback_id")
    private Integer feedbackId;

    @Column(name = "error_type")
    private String errorType;

    @Column(name = "original_text", columnDefinition = "TEXT")
    private String originalText;

    @Column(name = "suggested_text", columnDefinition = "TEXT")
    private String suggestedText;

    @Column(columnDefinition = "TEXT")
    private String explanation;
}
