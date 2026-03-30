package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.BookCreateRequest;
import site.viosmash.english.dto.request.FavoriteRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.BookPageResponse;
import site.viosmash.english.dto.response.BookResponse;
import site.viosmash.english.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Book endpoints")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get paginated books")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paged books", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
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
    public ResponseEntity<BaseResponse<?>> getListByGenre(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @PathVariable("id") Integer genreId
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getList(page, limit, null, genreId)));
    }

    @Operation(summary = "Create a book")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
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
    public ResponseEntity<BaseResponse<List<BookResponse>>> recommend() {
        return ResponseEntity.ok(BaseResponse.success(bookService.recommend()));
    }

    @Operation(summary = "Set/unset favorite for a book for current user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Favorite updated", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/v1/favorite")
    public ResponseEntity<BaseResponse<?>> favorite(@RequestBody FavoriteRequest req) {
        bookService.favorite(req.getBookId(), req.isFavorite());
        return ResponseEntity.ok(BaseResponse.success("OK"));
    }

    @Operation(summary = "get detail book by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detail book", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1/{id}")
    public ResponseEntity<BaseResponse<BookResponse>> getDetail(@PathVariable("id") int id) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getDetail(id)));
    }

    @Operation(summary = "Get pages by book id and page numbers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of pages", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1/{bookId}/pages")
    public ResponseEntity<BaseResponse<List<BookPageResponse>>> getPagesByBook(
            @PathVariable("bookId") int bookId,
            @RequestParam("pageNumbers") List<Integer> pageNumbers
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getPagesByBook(bookId, pageNumbers)));
    }
}
