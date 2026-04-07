package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.GenreCreateRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.GenreResponse;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.service.GenreService;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/genre")
@RequiredArgsConstructor
@Tag(name = "Genre", description = "Genre management")
public class GenreController {

    private final GenreService genreService;

    @Operation(summary = "Get list genres")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "list genres", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList() {
        return ResponseEntity.ok(BaseResponse.success(genreService.getList()));
    }

    @Operation(summary = "Create a genre")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Genre created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<GenreResponse>> create(@RequestBody GenreCreateRequest req) {
        GenreResponse r = genreService.create(req);
        return ResponseEntity.ok(BaseResponse.success(r));
    }

    @Operation(summary = "Update User Favorite Genres", security = { @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/v1/favorite-genres")
    public ResponseEntity<BaseResponse<Boolean>> updateFavoriteGenres(@RequestBody site.viosmash.english.dto.request.UpdateUserFavoriteGenresRequest req) {
        return ResponseEntity.ok(BaseResponse.success(genreService.updateUserFavoriteGenres(req)));
    }
}
