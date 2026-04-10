package site.viosmash.english.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckUpdateRequest {
    String title;

    int status;
    java.util.List<site.viosmash.english.dto.request.FlashcardUpdateRequest> flashcards;
}
