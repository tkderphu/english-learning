package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserCorrectionItemResponse {
    private Integer errorId;
    private String errorType;
    private String originalText;
    private String suggestedText;
    private String explanation;
    private LocalDateTime occurredAt;
    private Integer messageId;
    private Integer sessionId;
    private String sessionTitle;
    /** Hiển thị nguồn: Daily Conversation Chat, Chat: …, v.v. */
    private String sourceLabel;
}
