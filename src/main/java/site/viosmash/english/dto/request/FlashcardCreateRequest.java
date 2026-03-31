package site.viosmash.english.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardCreateRequest {
    String word;
    String phonetic;
    String meaning;
    String exampleSentence;
    String visualCueUrl;
    String note;
}
