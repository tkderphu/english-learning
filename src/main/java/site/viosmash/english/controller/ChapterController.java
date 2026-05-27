package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.viosmash.english.dto.request.ChapterCreateRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.ChapterService;

/**
 * ChapterController – Bộ điều khiển quản lý Chương sách.
 *
 * Nhóm các endpoint liên quan đến việc tạo và lấy danh sách các chương
 * thuộc về một cuốn sách cụ thể.
 *
 * Base path: /api/chapter
 */
@RestController
@RequestMapping("/api/chapter")
@RequiredArgsConstructor
@Tag(name = "Chapter", description = "Chapter endpoints")
public class ChapterController {

    private final ChapterService chapterService;

    @Operation(summary = "Get paginated chapters for a book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged chapters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @org.springframework.web.bind.annotation.GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList(
            @org.springframework.web.bind.annotation.RequestParam int bookId,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "1") int page,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(BaseResponse.success(chapterService.getList(bookId, page, limit)));
    }

    /**
     * Tạo chương mới – POST /api/chapter/v1
     *
     * Nhận bookId, title, description, number rồi gọi chapterService.create(),
     * trả về chapterId mới.
     *
     * @param req Request chứa thông tin chương (thuộc sách nào, số thứ tự, tiêu đề, mô tả)
     * @return BaseResponse chứa id của chương vừa tạo
     */
    @Operation(summary = "Create a chapter")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chapter created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<?>> create(@RequestBody ChapterCreateRequest req) {
        return ResponseEntity.ok(BaseResponse.success(chapterService.create(req)));
    }
}
