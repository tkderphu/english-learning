package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionMissionResponse {
    private String title;
    private String objective;
    private List<String> successCriteria;
    private String status;
}
