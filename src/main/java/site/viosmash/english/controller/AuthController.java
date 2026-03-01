package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.viosmash.english.dto.request.LoginRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints (login)")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login and receive JWT token")
    @PostMapping("/v1/login")
    public ResponseEntity<?> login(@jakarta.validation.Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.login(req)));
    }
}
