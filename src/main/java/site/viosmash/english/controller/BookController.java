package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.BookCreateRequest;
import site.viosmash.english.dto.response.BaseResponse;
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
    public ResponseEntity<BaseResponse<?>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(BaseResponse.success(bookService.getList(page, limit, keyword)));
    }

    @Operation(summary = "Create a book")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<BookResponse>> create(@RequestBody BookCreateRequest req) {
        BookResponse r = bookService.create(req);
        return ResponseEntity.ok(BaseResponse.success(r));
    }

    @Operation(summary = "Get history of books read by current user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paged history", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/v1/history")
    public ResponseEntity<BaseResponse<?>> history(
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
    public ResponseEntity<BaseResponse<List<?>>> recommend() {
        return ResponseEntity.ok(BaseResponse.success(bookService.recommend()));
    }
}
