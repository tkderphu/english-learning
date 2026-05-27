package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.PageRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.service.PageService;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import org.springframework.http.HttpStatus;

/**
 * PageController – Bộ điều khiển quản lý Trang sách.
 *
 * Cung cấp các endpoint để tạo trang (chứa nội dung text và audio) thuộc
 * về một chương sách, cũng như lấy danh sách các trang.
 *
 * Base path: /api/page
 */
@RestController
@RequestMapping("/api/page")
@RequiredArgsConstructor
@Tag(name = "Page", description = "Page endpoints")
public class PageController {

    private final PageService pageService;

    @Operation(summary = "Get list of pages by chapter ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pages list", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList(@RequestParam int chapterId,
                                                   @org.springframework.web.bind.annotation.RequestParam(defaultValue = "1") int page,
                                                   @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(BaseResponse.success(pageService.getListByChapterId(chapterId, page, limit)));
    }

    /**
     * Tạo trang mới – POST /api/page/v1
     *
     * Nhận chapterId, content, audioId, number, gọi pageService.create().
     * Nếu có lỗi I/O (ví dụ khi xử lý audio bằng Whisper), sẽ ném ServiceException.
     *
     * @param req Request chứa thông tin trang (chương ID, âm thanh ID, số thứ tự)
     * @return BaseResponse
     */
    @Operation(summary = "Create a page (multipart/form-data)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<?>> create(@RequestBody PageRequest req) {
        try {
            pageService.create(req);
            return ResponseEntity.ok(BaseResponse.success(null));
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create page: " + e.getMessage());
        }
    }
}
