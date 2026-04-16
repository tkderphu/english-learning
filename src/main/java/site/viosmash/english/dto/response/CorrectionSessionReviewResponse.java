package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CorrectionSessionReviewResponse {
    private Integer sessionId;
    /** Ví dụ: CONVERSATION WITH AI TUTOR */
    private String contextHeader;
    private String sessionTitle;
    private LocalDateTime sessionStartedAt;
    private LocalDateTime sessionEndedAt;
    private Integer durationMinutes;
    private Integer improvementCount;
    private List<SessionCorrectionDetailResponse> improvements;
}
