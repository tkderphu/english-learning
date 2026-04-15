package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FluencySignalsResponse {
    private Integer pauseDensity;
    private Integer fillerWords;
    private Integer continuousLength;
    private Integer flowProgress;
}
