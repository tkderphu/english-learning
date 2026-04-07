package site.viosmash.english.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckUpdateRequest {
    String title;
    String description;
    String coverImageUrl;
    int status;
    java.util.List<site.viosmash.english.dto.request.FlashcardUpdateRequest> flashcards;
}
