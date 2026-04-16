package site.viosmash.english.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SessionMissionRequest {
    private String title;
    private String objective;
    private List<String> successCriteria;
}
