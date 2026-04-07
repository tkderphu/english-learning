package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class UserLearnedWordPatchRequest {

    private Boolean favorite;

    private Boolean needsAttention;
}
