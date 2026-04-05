package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.LevelCreateRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.LevelResponse;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.service.LevelService;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/api/level")
@RequiredArgsConstructor
@Tag(name = "Level", description = "Level management")
public class LevelController {

    private final LevelService levelService;

    @Operation(summary = "Get all levels")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of levels", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<List<LevelResponse>>> getLevels() {
        return ResponseEntity.ok(BaseResponse.success(levelService.getLevels()));
    }

    @Operation(summary = "Create a level")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Level created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<LevelResponse>> create(@RequestBody LevelCreateRequest req) {
        LevelResponse r = levelService.create(req);
        return ResponseEntity.ok(BaseResponse.success(r));
    }

    @Operation(summary = "Update User Level", security = { @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/v1")
    public ResponseEntity<BaseResponse<Boolean>> updateLevel(@RequestBody site.viosmash.english.dto.request.UpdateUserLevelRequest req) {
        return ResponseEntity.ok(BaseResponse.success(levelService.updateUserLevel(req)));
    }
}
