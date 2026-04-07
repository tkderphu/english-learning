package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import site.viosmash.english.dto.request.DeckCreateRequest;
import site.viosmash.english.dto.request.DeckStudyCompleteRequest;
import site.viosmash.english.dto.request.DeckUpdateRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.DeckService;
import site.viosmash.english.service.FlashcardService;
import site.viosmash.english.util.Util;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deck")
@Tag(name = "Deck", description = "Deck and Flashcard management endpoints")
public class DeckController {

    private final DeckService deckService;
    private final FlashcardService flashcardService;
    private final Util util;

    @Operation(summary = "Create a new deck with flashcards", security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/v1")
    public ResponseEntity<?> createDeck(@jakarta.validation.Valid @RequestBody DeckCreateRequest req) {
        Integer userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(deckService.createDeckWithFlashcards(userId, req)));
    }

    @Operation(summary = "Get list of all active decks", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList() {
        Integer userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(deckService.getAllActiveDecks(userId)));
    }

    @Operation(summary = "Get deck details by ID", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/v1/{id}")
    public ResponseEntity<BaseResponse<?>> getDeckById(@PathVariable("id") Integer id) {
        Integer userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(deckService.getDeckResponseById(userId, id)));
    }

    @Operation(summary = "Update deck information", security = { @SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/v1/{id}")
    public ResponseEntity<BaseResponse<?>> updateDeck(
            @PathVariable("id") Integer id,
            @jakarta.validation.Valid @RequestBody DeckUpdateRequest req) {
        Integer userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(deckService.updateDeck(userId, id, req)));
    }

    @Operation(summary = "Soft delete a deck", security = { @SecurityRequirement(name = "bearerAuth") })
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<BaseResponse<?>> deleteDeck(@PathVariable("id") Integer id) {
        Integer userId = util.getCurrentUser().getId();
        deckService.deleteDeck(userId, id);
        return ResponseEntity.ok(BaseResponse.success("Deleted successfully"));
    }

    @Operation(
            summary = "Get ALL flashcards in a deck",
            description = "Returns all active flashcards without spaced repetition filtering. Used for Quizlet-style study where every session starts fresh.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @GetMapping("/v1/{id}/flashcards")
    public ResponseEntity<BaseResponse<?>> getAllFlashcards(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(BaseResponse.success(flashcardService.getAllFlashcardsInDeck(id)));
    }

    @Operation(
            summary = "Báo cáo hoàn thành phiên học flashcard",
            description = "Ghi nhận thời lượng và (tuỳ chọn) điểm quiz vào lịch sử học / heatmap.",
            security = { @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth") }
    )
    @PostMapping("/v1/{id}/study-complete")
    public ResponseEntity<BaseResponse<String>> studyComplete(
            @PathVariable("id") Integer deckId,
            @Valid @RequestBody DeckStudyCompleteRequest req
    ) {
        Integer userId = util.getCurrentUser().getId();
        deckService.recordStudySessionComplete(userId, deckId, req);
        return ResponseEntity.ok(BaseResponse.success("OK"));
    }
}