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

@RestController
@RequestMapping("/api/chapter")
@RequiredArgsConstructor
@Tag(name = "Chapter", description = "Chapter endpoints")
public class ChapterController {

    private final ChapterService chapterService;

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
