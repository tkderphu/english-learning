package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardResponse {
    private int id;
    private String term;
    private String definition;
    private String imageUrl;
    private int status;
}
