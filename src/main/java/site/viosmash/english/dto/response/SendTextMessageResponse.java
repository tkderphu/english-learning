package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendTextMessageResponse {
    private Integer sessionId;
    private Integer turnNumber;
    private ChatMessageItemResponse userMessage;
    private ChatMessageItemResponse aiMessage;
    private FeedbackResponse feedback;
}