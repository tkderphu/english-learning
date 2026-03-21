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
import site.viosmash.english.dto.request.AuthorCreateRequest;
import site.viosmash.english.dto.response.AuthorResponse;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.AuthorService;

@RestController
@RequestMapping("/api/author")
@RequiredArgsConstructor
@Tag(name = "Author", description = "Author management")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Get paginated authors")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paged authors", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<Page<AuthorResponse>>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String keyword
    ) {
        Page<AuthorResponse> p = authorService.page(page, limit, keyword);
        return ResponseEntity.ok(BaseResponse.success(p));
    }

    @Operation(summary = "Create an author")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Author created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<AuthorResponse>> create(@RequestBody AuthorCreateRequest req) {
        AuthorResponse r = authorService.create(req);
        return ResponseEntity.ok(BaseResponse.success(r));
    }
}
