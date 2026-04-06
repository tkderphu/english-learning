package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserLearnedWordResponse {
    private Integer id;
    private String term;
    private String phonetic;
    private String definition;
    private String sourceModule;
    private Boolean favorite;
    private Boolean needsAttention;
    private String audioUrl;
    private LocalDateTime createdAt;
}
