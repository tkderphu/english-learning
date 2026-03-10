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
import site.viosmash.english.dto.request.LoginRequest;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.AuthService;
import site.viosmash.english.service.ForgotPasswordService;
import site.viosmash.english.dto.request.ForgotPasswordRequest;
import site.viosmash.english.dto.request.VerifyOtpRequest;
import site.viosmash.english.dto.request.ResetPasswordRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints (login)")
public class AuthController {

    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;

    @Operation(summary = "Login and receive JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login success", content = @Content),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @PostMapping("/v1/login")
    public ResponseEntity<?> login(@jakarta.validation.Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.login(req)));
    }

    @PostMapping("/v1/forgot-password/request-otp")
    @Operation(summary = "Request OTP for forgot password")
    public ResponseEntity<?> requestOtp(@RequestBody ForgotPasswordRequest req) {
        forgotPasswordService.requestOtp(req.getEmail());
        return ResponseEntity.ok(BaseResponse.success("OTP đã được gửi nếu email tồn tại"));
    }

    @PostMapping("/v1/forgot-password/verify-otp")
    @Operation(summary = "Verify OTP for forgot password")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {
        forgotPasswordService.verifyOtp(req.getEmail(), req.getOtp());
        return ResponseEntity.ok(BaseResponse.success("OTP hợp lệ"));
    }

    @PostMapping("/v1/forgot-password/reset")
    @Operation(summary = "Reset password using OTP")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest req) {
        forgotPasswordService.resetPassword(req.getEmail(), req.getOtp(), req.getNewPassword());
        return ResponseEntity.ok(BaseResponse.success("Mật khẩu đã được cập nhật"));
    }
}
