package site.viosmash.english.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubtitleGenerationRequest {

    private String audioUrl;

    private String language;
}