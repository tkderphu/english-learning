package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionCorrectionDetailResponse {
    private Integer errorId;
    /** Nhãn nhóm: GRAMMAR, VOCABULARY, … */
    private String category;
    private String originalText;
    private String suggestedText;
    private String explanation;
}
