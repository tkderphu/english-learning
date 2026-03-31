package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteChatSessionResponse {
    private Integer sessionId;
    private String status;
}

