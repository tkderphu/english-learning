package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.UserTopicCreateRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.entity.UserTopic;
import site.viosmash.english.service.UserTopicService;

@RestController
@RequestMapping("/api/user-topic")
@RequiredArgsConstructor
@Tag(name = "UserTopic", description = "User-topic relations")
public class UserTopicController {

    private final UserTopicService userTopicService;

    @Operation(summary = "Create user-topic relation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<BaseResponse<UserTopic>> create(@RequestBody UserTopicCreateRequest req) {
        UserTopic r = userTopicService.create(req);
        return ResponseEntity.ok(BaseResponse.success(r));
    }
}
