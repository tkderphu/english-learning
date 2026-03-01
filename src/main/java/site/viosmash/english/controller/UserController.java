package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    @PostMapping("/v1")
    public ResponseEntity<?> createUser(@jakarta.validation.Valid @RequestBody UserCreateRequest req) {
        return ResponseEntity.ok(BaseResponse.success(userService.create(req)));
    }


    @Operation(summary = "Get paginated list of users", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/v1")
    public ResponseEntity<BaseResponse<?>> getList(@PageableDefault()Pageable pageable,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) Integer role,
                                                      @RequestParam(required = false) Integer status) {
        return ResponseEntity.ok(BaseResponse.success(userService.getList(pageable, keyword, role, status)));
    }
}
