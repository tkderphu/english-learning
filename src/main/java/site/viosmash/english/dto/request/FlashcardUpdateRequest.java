package site.viosmash.english.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardUpdateRequest {
    Integer id; // Null means it's a new flashcard
    String term;
    String definition;
    String imageUrl;
    Integer status; // 1 for active, 0 for deleted
}
