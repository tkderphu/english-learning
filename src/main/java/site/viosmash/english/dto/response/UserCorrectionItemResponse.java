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
    private Long occurredAtEpochMs;
    private Integer messageId;
    private Integer sessionId;
    private String sessionTitle;
    /** Nguồn hiển thị (chỉ AI Chat): ví dụ Daily Conversation Chat, Chat: … */
    private String sourceLabel;
}
