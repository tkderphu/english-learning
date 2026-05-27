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

/**
 * Controller quản lý các endpoint liên quan đến Bộ thẻ (Deck) và Flashcard.
 * Cung cấp các chức năng CRUD cho bộ thẻ và ghi nhận kết quả học tập.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deck")
@Tag(name = "Deck", description = "Deck and Flashcard management endpoints")
public class DeckController {

    private final DeckService deckService;
    private final FlashcardService flashcardService;
    private final Util util;

    /**
     * Tạo mới một bộ thẻ (Deck) cùng với danh sách các thẻ từ vựng (Flashcard) bên trong.
     *
     * @param req Dữ liệu đầu vào bao gồm tiêu đề bộ thẻ và danh sách các thẻ.
     * @return ResponseEntity chứa thông tin bộ thẻ vừa tạo.
     */
    @Operation(summary = "Create a new deck with flashcards", security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/v1")
    public ResponseEntity<?> createDeck(@jakarta.validation.Valid @RequestBody DeckCreateRequest req) {
        Integer userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(deckService.createDeckWithFlashcards(userId, req)));
    }

    /**
     * Lấy danh sách toàn bộ các bộ thẻ đang hoạt động (Active) của người dùng hiện tại.
     * Hỗ trợ tìm kiếm gần đúng theo tiêu đề bộ thẻ nếu truyền tham số search.
     *
     * @param search (Tuỳ chọn) Từ khóa tìm kiếm theo tiêu đề bộ thẻ.
     * @return ResponseEntity chứa danh sách các bộ thẻ.
     */
    @Operation(summary = "Get list of all active decks", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList(@RequestParam(value = "search", required = false) String search) {
        Integer userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(deckService.getAllActiveDecks(userId, search)));
    }

    /**
     * Lấy chi tiết thông tin của một bộ thẻ cụ thể dựa vào ID.
     * Yêu cầu người dùng hiện tại phải là chủ sở hữu của bộ thẻ đó.
     *
     * @param id ID của bộ thẻ cần lấy.
     * @return ResponseEntity chứa chi tiết bộ thẻ.
     */
    @Operation(summary = "Get deck details by ID", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/v1/{id}")
    public ResponseEntity<BaseResponse<?>> getDeckById(@PathVariable("id") Integer id) {
        Integer userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(deckService.getDeckResponseById(userId, id)));
    }

    /**
     * Cập nhật thông tin của một bộ thẻ, bao gồm cả tiêu đề và danh sách flashcard.
     * Hỗ trợ thêm mới, sửa đổi hoặc xóa (soft-delete) flashcard bên trong.
     *
     * @param id  ID của bộ thẻ cần cập nhật.
     * @param req Dữ liệu cập nhật mới.
     * @return ResponseEntity chứa thông tin bộ thẻ sau khi cập nhật.
     */
    @Operation(summary = "Update deck information", security = { @SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/v1/{id}")
    public ResponseEntity<BaseResponse<?>> updateDeck(
            @PathVariable("id") Integer id,
            @jakarta.validation.Valid @RequestBody DeckUpdateRequest req) {
        Integer userId = util.getCurrentUser().getId();
        return ResponseEntity.ok(BaseResponse.success(deckService.updateDeck(userId, id, req)));
    }

    /**
     * Xóa mềm (Soft delete) một bộ thẻ. Bộ thẻ sẽ bị ẩn khỏi danh sách 
     * nhưng vẫn giữ lại trong cơ sở dữ liệu để đảm bảo toàn vẹn dữ liệu thống kê.
     *
     * @param id ID của bộ thẻ cần xóa.
     * @return ResponseEntity xác nhận xóa thành công.
     */
    @Operation(summary = "Soft delete a deck", security = { @SecurityRequirement(name = "bearerAuth") })
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<BaseResponse<?>> deleteDeck(@PathVariable("id") Integer id) {
        Integer userId = util.getCurrentUser().getId();
        deckService.deleteDeck(userId, id);
        return ResponseEntity.ok(BaseResponse.success("Deleted successfully"));
    }

    /**
     * Lấy toàn bộ các thẻ từ vựng (Flashcard) thuộc về một bộ thẻ.
     * Sử dụng cho phiên học ôn tập kiểu lật thẻ (Quizlet-style).
     *
     * @param id ID của bộ thẻ.
     * @return ResponseEntity chứa danh sách toàn bộ Flashcard trong bộ thẻ đó.
     */
    @Operation(
            summary = "Get ALL flashcards in a deck",
            description = "Returns all active flashcards without spaced repetition filtering. Used for Quizlet-style study where every session starts fresh.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @GetMapping("/v1/{id}/flashcards")
    public ResponseEntity<BaseResponse<?>> getAllFlashcards(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(BaseResponse.success(flashcardService.getAllFlashcardsInDeck(id)));
    }

    /**
     * Ghi nhận kết quả của một phiên học Flashcard. 
     * Hệ thống sẽ lưu trữ thời gian học và điểm số (nếu có) vào lịch sử học tập (Heatmap).
     *
     * @param deckId ID của bộ thẻ vừa học xong.
     * @param req    Thông tin báo cáo bao gồm thời lượng (durationSeconds) và kết quả (score).
     * @return ResponseEntity thông báo OK.
     */
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