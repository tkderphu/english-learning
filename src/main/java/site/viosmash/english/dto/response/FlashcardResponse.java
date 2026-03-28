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
    private String word;
    private String phonetic;
    private String partOfSpeech;
    private String meaning;
    private String exampleSentence;
    private String visualCueUrl;
    private String note;
    private int status;
}
