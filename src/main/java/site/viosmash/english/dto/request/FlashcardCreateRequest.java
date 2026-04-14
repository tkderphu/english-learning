package site.viosmash.english.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardCreateRequest {
    String term;
    String definition;
    String imageUrl;
}
