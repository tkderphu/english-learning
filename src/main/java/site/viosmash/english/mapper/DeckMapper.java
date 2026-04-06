package site.viosmash.english.mapper;

import org.springframework.stereotype.Component;

import site.viosmash.english.dto.response.DeckResponse;
import site.viosmash.english.entity.Deck;

@Component
public class DeckMapper {

    public DeckResponse toResponse(Deck deck) {
        if (deck == null) {
            return null;
        }

        java.util.List<site.viosmash.english.dto.response.FlashcardResponse> flashcardResponses = null;
        if (deck.getFlashcards() != null) {
            flashcardResponses = deck.getFlashcards().stream()
                    .filter(f -> f.getStatus() == 1) // Only return active flashcards
                    .map(f -> site.viosmash.english.dto.response.FlashcardResponse.builder()
                            .id(f.getId())
                            .word(f.getWord())
                            .phonetic(f.getPhonetic())
                            .meaning(f.getMeaning())
                            .exampleSentence(f.getExampleSentence())
                            .visualCueUrl(f.getVisualCueUrl())
                            .note(f.getNote())
                            .status(f.getStatus())
                            .build())
                    .collect(java.util.stream.Collectors.toList());
        }

        return DeckResponse.builder()
                .id(deck.getId())
                .userId(deck.getUser() != null ? deck.getUser().getId() : null)
                .title(deck.getTitle())
                .description(deck.getDescription())
                .coverImageUrl(deck.getCoverImageUrl())
                .totalWords(deck.getTotalWords())
                .status(deck.getStatus())
                .createdAt(deck.getCreatedAt())
                .modifiedAt(deck.getModifiedAt())
                .flashcards(flashcardResponses)
                .build();
    }
}
