package site.viosmash.english.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLearnedWordCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String term;

    @Size(max = 128)
    private String phonetic;

    @Size(max = 4000)
    private String definition;

    @Size(max = 64)
    private String sourceModule;

    private Boolean favorite;

    private Boolean needsAttention;

    @Size(max = 1024)
    private String audioUrl;
}
