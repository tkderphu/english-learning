package site.viosmash.english.controller;

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
public class AuthController {

    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/v1/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.login(req)));
    }

    @PostMapping("/v1/forgot-password/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody ForgotPasswordRequest req) {
        forgotPasswordService.requestOtp(req.getEmail());
        return ResponseEntity.ok(BaseResponse.success("OTP đã được gửi nếu email tồn tại"));
    }

    @PostMapping("/v1/forgot-password/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {
        forgotPasswordService.verifyOtp(req.getEmail(), req.getOtp());
        return ResponseEntity.ok(BaseResponse.success("OTP hợp lệ"));
    }

    @PostMapping("/v1/forgot-password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest req) {
        forgotPasswordService.resetPassword(req.getEmail(), req.getOtp(), req.getNewPassword());
        return ResponseEntity.ok(BaseResponse.success("Mật khẩu đã được cập nhật"));
    }
}
