package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.SentenceService;

@RestController
@RequestMapping("/api/sentence")
@RequiredArgsConstructor
@Tag(name = "Sentence", description = "Sentence endpoints")
public class SentenceController {

    private final SentenceService sentenceService;

    @Operation(summary = "Get list of sentences by page ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sentences list", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList(@RequestParam int pageId) {
        return ResponseEntity.ok(BaseResponse.success(sentenceService.getListByPageId(pageId)));
    }
}
