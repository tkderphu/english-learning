package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateChatSessionResponse {
    private Integer sessionId;
    private String title;
    private String aiRole;
    private String instruction;
    private String status;
    private Integer currentTurn;
    private String goalType;
    private String focusSkill;
    private String coachingMode;
    private Boolean fluencyMode;
    private Integer targetDurationMinutes;
    private SessionMissionResponse mission;
}