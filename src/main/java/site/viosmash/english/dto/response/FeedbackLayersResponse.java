package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackLayersResponse {
    private String layer1Tip;
    private String layer2Explanation;
    private String layer2Example;
    private String layer3Checkpoint;
    private String layer3NextAction;
}
