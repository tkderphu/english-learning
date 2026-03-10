package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.viosmash.english.dto.request.UserCreateRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping("/v1")
    public ResponseEntity<?> createUser(@jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody UserCreateRequest req) {
        return ResponseEntity.ok(BaseResponse.success(userService.create(req)));
    }


    @Operation(summary = "Get paginated list of users", security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paged user list",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList(@PageableDefault()Pageable pageable,
                                                      @Parameter(description = "Keyword to search name or email") @RequestParam(required = false) String keyword,
                                                      @Parameter(description = "Filter by role") @RequestParam(required = false) Integer role,
                                                      @Parameter(description = "Filter by status") @RequestParam(required = false) Integer status) {
        return ResponseEntity.ok(BaseResponse.success(userService.getList(pageable, keyword, role, status)));
    }
}
