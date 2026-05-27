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

/**
 * AuthController – Bộ điều khiển xác thực người dùng.
 *
 * <p>Nhóm các endpoint liên quan đến đăng nhập và quy trình quên mật khẩu
 * (yêu cầu OTP → xác minh OTP → đặt lại mật khẩu). Mọi endpoint trong
 * controller này đều <b>không yêu cầu JWT</b> (public access), vì chúng
 * được sử dụng trước hoặc trong quá trình xác thực.</p>
 *
 * <p>Base path: {@code /api/auth}</p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints (login)")
public class AuthController {

    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;

    /**
     * Đăng nhập – {@code POST /api/auth/v1/login}
     *
     * <p>Nhận request đăng nhập (email + mật khẩu), validate bằng {@code @Valid},
     * rồi uỷ quyền cho {@link AuthService#login(LoginRequest)} để xác thực.
     * Khi thành công, trả về {@code AuthResponse} gồm {@code accessToken},
     * {@code refreshToken} và thông tin cơ bản của người dùng.</p>
     *
     * @param req Body chứa {@code email} và {@code password}
     * @return {@code 200 OK} với {@code AuthResponse} | {@code 401} nếu sai thông tin
     */
    @Operation(summary = "Login and receive JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login success", content = @Content),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @PostMapping("/v1/login")
    public ResponseEntity<?> login(@jakarta.validation.Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.login(req)));
    }

    /**
     * Yêu cầu OTP quên mật khẩu – {@code POST /api/auth/v1/forgot-password/request-otp}
     *
     * <p>Kiểm tra email có tồn tại trong hệ thống, tạo mã OTP 6 chữ số ngẫu nhiên,
     * lưu vào bảng {@code password_reset_otps} (xóa OTP cũ trước) rồi gọi
     * {@link ForgotPasswordService#requestOtp(String)} để gửi email qua
     * {@code MailService.sendOtpEmail()}. OTP có hiệu lực trong {@code app.otp.ttl.minutes}
     * phút (mặc định 5 phút).</p>
     *
     * @param req Body chứa {@code email} cần nhận OTP
     * @return {@code 200 OK} với thông báo thành công
     */
    @PostMapping("/v1/forgot-password/request-otp")
    @Operation(summary = "Request OTP for forgot password")
    public ResponseEntity<?> requestOtp(@RequestBody ForgotPasswordRequest req) {
        forgotPasswordService.requestOtp(req.getEmail());
        return ResponseEntity.ok(BaseResponse.success("OTP đã được gửi nếu email tồn tại"));
    }

    /**
     * Xác minh OTP – {@code POST /api/auth/v1/forgot-password/verify-otp}
     *
     * <p>Kiểm tra OTP gửi lên có khớp với bản ghi trong CSDL, chưa được dùng
     * ({@code used = false}) và chưa hết hạn ({@code expiresAt > now}).
     * Nếu OTP đã hết hạn, ném {@code 410 Gone}; nếu không hợp lệ, ném
     * {@code 406 Not Acceptable}.</p>
     *
     * @param req Body chứa {@code email} và {@code otp}
     * @return {@code 200 OK} nếu OTP hợp lệ
     */
    @PostMapping("/v1/forgot-password/verify-otp")
    @Operation(summary = "Verify OTP for forgot password")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {
        forgotPasswordService.verifyOtp(req.getEmail(), req.getOtp());
        return ResponseEntity.ok(BaseResponse.success("OTP hợp lệ"));
    }

    /**
     * Đặt lại mật khẩu – {@code POST /api/auth/v1/forgot-password/reset}
     *
     * <p>Kiểm tra lại OTP (cùng logic với verify), mã hoá mật khẩu mới bằng
     * {@code PasswordEncoder.encode()} (BCrypt), lưu vào bảng {@code users},
     * sau đó đánh dấu OTP là {@code used = true} để ngăn tái sử dụng.</p>
     *
     * @param req Body chứa {@code email}, {@code otp} và {@code newPassword}
     * @return {@code 200 OK} với thông báo cập nhật thành công
     */
    @PostMapping("/v1/forgot-password/reset")
    @Operation(summary = "Reset password using OTP")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest req) {
        forgotPasswordService.resetPassword(req.getEmail(), req.getOtp(), req.getNewPassword());
        return ResponseEntity.ok(BaseResponse.success("Mật khẩu đã được cập nhật"));
    }
}
