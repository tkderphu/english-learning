package site.viosmash.english.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatSessionRequest {
    private Integer userId;
    private Integer scenarioId;
    private Integer maxTurns;
}