package site.viosmash.english.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_chat_messages")
@Getter
@Setter
public class AiChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "sender_type")
    private String senderType;

    @Column(name = "input_type")
    private String inputType;

    @Column(name = "turn_number")
    private Integer turnNumber;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "normalized_content", columnDefinition = "TEXT")
    private String normalizedContent;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "audio_duration")
    private Integer audioDuration;

    @Column(name = "stt_transcript", columnDefinition = "TEXT")
    private String sttTranscript;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
