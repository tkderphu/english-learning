package site.viosmash.english.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckCreateRequest {
    String title;
    String coverImageUrl;
    List<FlashcardCreateRequest> flashcards;
}
