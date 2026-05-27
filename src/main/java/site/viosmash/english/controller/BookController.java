package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import site.viosmash.english.dto.request.BookCreateRequest;
import site.viosmash.english.dto.request.BookReadingProgressRequest;
import site.viosmash.english.dto.response.AuthorResponse;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.BookPageResponse;
import site.viosmash.english.dto.response.BookResponse;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.service.BookService;
import site.viosmash.english.util.Util;

import java.util.List;

/**
 * BookController – Bộ điều khiển quản lý Sách.
 *
 * Cung cấp các endpoint để lấy danh sách sách, tạo sách mới, xem lịch sử đọc,
 * gợi ý sách, và quản lý danh sách yêu thích.
 *
 * Base path: /api/book
 */
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Book endpoints")
/**
 * Handle book-related APIs for home, detail, search and reading flows.
 */
public class BookController {

    private final BookService bookService;
    private final Util util;

    @Operation(summary = "Get paginated books")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paged books", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
    /** Return paginated books with optional keyword filter. */
    public ResponseEntity<BaseResponse<?>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getList(page, limit, keyword, null)));
    }

    @Operation(summary = "Get paginated books by genreId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged books", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1/genre/{id}")
    /** Return paginated books by genre for books-by-genre screen. */
    public ResponseEntity<BaseResponse<?>> getListByGenre(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @PathVariable("id") Integer genreId
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getList(page, limit, null, genreId)));
    }

    /**
     * Tạo sách mới – POST /api/book/v1
     *
     * Nhận request tạo sách, gọi bookService.create(). Không yêu cầu xác thực (public endpoint).
     *
     * @param req Thông tin sách mới (tiêu đề, tác giả, thể loại, coverUrl...)
     * @return BaseResponse
     */
    @Operation(summary = "Create a book")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
    /** Create a new book with author/genre relations. */
    public ResponseEntity<BaseResponse<?>> create(@RequestBody BookCreateRequest req) {
        bookService.create(req);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(summary = "Get history of books read by current user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paged history", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/v1/history")
    /** Return reading history of current user. */
    public ResponseEntity<BaseResponse<?>>history(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getHistory(page, limit)));
    }

    @Operation(summary = "Recommend books for current user based on profile")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of recommended books", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/v1/recommend")
    /** Return recommendation list for current user home feed. */
    public ResponseEntity<BaseResponse<?>> recommend(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.recommend(page, limit)));
    }

    @Operation(summary = "Get active authors for books")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of authors", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1/authors")
    /** Return paginated active authors for author listing screens. */
    public ResponseEntity<BaseResponse<PageResponse<AuthorResponse>>> getAuthors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getAuthors(page, limit)));
    }

    @Operation(summary = "Get paginated books by authorId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged books", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1/authors/{authorId}/books")
    /** Return paginated books by selected author. */
    public ResponseEntity<BaseResponse<PageResponse<BookResponse>>> getBooksByAuthor(
            @PathVariable("authorId") int authorId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getBooksByAuthor(authorId, page, limit)));
    }

    @Operation(summary = "Get current user's favorite books")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paged favorite books", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/v1/favorites")
    /** Return current user's favorite books. */
    public ResponseEntity<BaseResponse<PageResponse<BookResponse>>> getFavorites(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getFavorites(page, limit)));
    }

    @Operation(summary = "Set/unset favorite for a book for current user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Favorite updated", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PutMapping("/v1/favorite/{bookId}")
    /** Mark or unmark a book as favorite for current user. */
    public ResponseEntity<BaseResponse<Boolean>> favorite(
            @PathVariable("bookId") int bookId,
            @RequestParam("isFavorite") boolean isFavorite
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.favorite(bookId, isFavorite)));
    }

    @Operation(summary = "get detail book by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detail book", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1/{id}")
    /** Return detail information of a book by id. */
    public ResponseEntity<BaseResponse<BookResponse>> getDetail(@PathVariable("id") int id) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getDetail(id)));
    }

    @Operation(summary = "Get pages by book id with offset and limit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of pages", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1/{bookId}/pages")
    /** Return chunked pages to support reading screen pagination. */
    public ResponseEntity<BaseResponse<List<BookPageResponse>>> getPagesByBook(
            @PathVariable("bookId") int bookId,
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getPagesByBook(bookId, offset, limit)));
    }

    /**
     * Cập nhật tiến độ đọc sách – PATCH /api/book/v1/{bookId}/progress
     *
     * Cập nhật trang đang đọc vào bảng book_progress.
     * Nếu thời lượng đọc (durationSeconds) >= 30s, gọi logBookReadingSession
     * để ghi nhận hoạt động vào biểu đồ heatmap.
     *
     * @param bookId ID của sách
     * @param req Thông tin tiến độ (trang cuối cùng, thời gian bắt đầu, thời lượng)
     * @return BaseResponse "OK"
     */
    @Operation(
            summary = "Cập nhật tiến độ đọc sách",
            description = "Lưu trang đang đọc; nếu durationSeconds >= 30s thì ghi thêm hoạt động BOOK cho heatmap.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PatchMapping("/v1/{bookId}/progress")
    /** Save reading progress and optional activity log for heatmap. */
    public ResponseEntity<BaseResponse<String>> updateReadingProgress(
            @PathVariable("bookId") int bookId,
            @Valid @RequestBody BookReadingProgressRequest req
    ) {
        int userId = util.getCurrentUser().getId();
        bookService.recordReadingProgress(userId, bookId, req);
        return ResponseEntity.ok(BaseResponse.success("OK"));
    }
}
