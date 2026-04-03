package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiScenarioResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer topicId;
    private Integer levelId;
    private String levelName;
    private String type;
    private String aiRole;
    private String instruction;
    private String iconUrl;
}