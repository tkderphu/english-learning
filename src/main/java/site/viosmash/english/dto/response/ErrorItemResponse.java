package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorItemResponse {
    private String type;
    private String originalText;
    private String suggestedText;
    private String explanation;
}