package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageItemResponse {
    private Integer id;
    private String senderType;
    private String inputType;
    private Integer turnNumber;
    private String content;
    private LocalDateTime createdAt;
}