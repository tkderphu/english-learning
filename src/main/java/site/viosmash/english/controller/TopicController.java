package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.TopicCreateRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.dto.response.TopicResponse;
import site.viosmash.english.service.TopicService;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/topic")
@RequiredArgsConstructor
@Tag(name = "Topic", description = "Topic management")
public class TopicController {

    private final TopicService topicService;

    @Operation(summary = "Get paginated topics")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paged topics", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String keyword
    ) {
        Page<TopicResponse> p = topicService.page(page, limit, keyword);
        return ResponseEntity.ok(BaseResponse.success(p));
    }

    @Operation(summary = "Create a topic")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Topic created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<TopicResponse>> create(@RequestBody TopicCreateRequest req) {
        TopicResponse r = topicService.create(req);
        return ResponseEntity.ok(BaseResponse.success(r));
    }
}
