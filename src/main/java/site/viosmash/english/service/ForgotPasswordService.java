package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.viosmash.english.entity.PasswordResetOtp;
import site.viosmash.english.entity.User;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.PasswordResetOtpRepository;
import site.viosmash.english.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * ForgotPasswordService – Xử lý toàn bộ quy trình quên mật khẩu.
 *
 * <p>Quy trình gồm 3 bước:
 * <ol>
 *   <li><b>requestOtp</b>: Xác nhận email tồn tại → tạo OTP 6 chữ số → gửi email.</li>
 *   <li><b>verifyOtp</b>: Kiểm tra OTP còn hiệu lực và chưa được dùng.</li>
 *   <li><b>resetPassword</b>: Mã hoá mật khẩu mới và đánh dấu OTP đã dùng.</li>
 * </ol>
 *
 * <p>OTP được lưu trong bảng {@code password_reset_otps} với trường
 * {@code expires_at} và {@code used} để kiểm soát vòng đời.</p>
 */
@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final PasswordResetOtpRepository otpRepository;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    /** Thời gian hiệu lực của OTP (phút), mặc định 5 phút nếu không cấu hình. */
    @Value("${app.otp.ttl.minutes:5}")
    private int otpTtlMinutes;

    /**
     * Tạo và gửi OTP đặt lại mật khẩu qua email.
     *
     * <p>Các bước thực hiện:
     * <ol>
     *   <li>Tìm user theo email; ném {@code 404} nếu không tồn tại.</li>
     *   <li>Xoá toàn bộ OTP cũ của email này ({@code deleteAllByEmail}).</li>
     *   <li>Sinh OTP ngẫu nhiên 6 chữ số trong khoảng [100000, 999999].</li>
     *   <li>Lưu vào {@code password_reset_otps} với {@code expiresAt = now + otpTtlMinutes}.</li>
     *   <li>Gọi {@code mailService.sendOtpEmail()} để gửi email; ném {@code 500} nếu lỗi gửi mail.</li>
     * </ol>
     *
     * @param email Email của người dùng cần đặt lại mật khẩu
     * @throws ServiceException {@code 404} nếu email không tồn tại;
     *                          {@code 500} nếu gửi email thất bại
     */
    @Transactional
    public void requestOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Email không tồn tại"));

        // cleanup previous otps
        otpRepository.deleteAllByEmail(email);

        String otp = generateOtp();
        PasswordResetOtp pr = new PasswordResetOtp()
                .setEmail(email)
                .setOtp(otp)
                .setExpiresAt(LocalDateTime.now().plusMinutes(otpTtlMinutes))
                .setUsed(false);
        otpRepository.save(pr);

        String subject = "Your password reset OTP";
        String text = String.format("Hi %s,\n\nYour OTP to reset password is: %s\nThis OTP is valid for %d minutes.\n\nIf you didn't request this, please ignore.",
                user.getFullName() == null ? "user" : user.getFullName(), otp, otpTtlMinutes);

        try {
            mailService.sendOtpEmail(email, subject, text);
        } catch (Exception ex) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể gửi email OTP");
        }
    }

    /**
     * Sinh mã OTP ngẫu nhiên 6 chữ số.
     *
     * @return Chuỗi số nguyên ngẫu nhiên trong khoảng [100000, 999999]
     */
    private String generateOtp() {
        Random r = new Random();
        int number = 100000 + r.nextInt(900000);
        return String.valueOf(number);
    }

    /**
     * Xác minh OTP do người dùng cung cấp.
     *
     * <p>Tìm bản ghi OTP theo {@code email + otp + used=false} và sắp xếp
     * theo {@code createdAt DESC} (lấy OTP mới nhất). Sau đó kiểm tra thời hạn.</p>
     *
     * @param email Email đã yêu cầu OTP
     * @param otp   Mã OTP 6 chữ số người dùng nhập vào
     * @throws ServiceException {@code 406 Not Acceptable} nếu OTP không hợp lệ;
     *                          {@code 410 Gone} nếu OTP đã hết hạn
     */
    public void verifyOtp(String email, String otp) {
        PasswordResetOtp pr = otpRepository.findFirstByEmailAndOtpAndUsedIsFalseOrderByCreatedAtDesc(email, otp)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_ACCEPTABLE, "OTP không hợp lệ"));
        if (pr.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ServiceException(HttpStatus.GONE, "OTP đã hết hạn");
        }
    }

    /**
     * Đặt lại mật khẩu mới cho người dùng sau khi xác minh OTP thành công.
     *
     * <p>Tìm lại bản ghi OTP để xác minh lần cuối, sau đó:
     * <ol>
     *   <li>Mã hoá {@code newPassword} bằng BCrypt ({@code passwordEncoder.encode()}).</li>
     *   <li>Lưu mật khẩu mới vào bảng {@code users}.</li>
     *   <li>Đánh dấu OTP {@code used = true} để ngăn tái sử dụng.</li>
     * </ol>
     *
     * @param email       Email của tài khoản cần đặt lại mật khẩu
     * @param otp         Mã OTP đã được xác minh ở bước trước
     * @param newPassword Mật khẩu mới (plain text, sẽ được mã hoá trước khi lưu)
     */
    public void resetPassword(String email, String otp, String newPassword) {
        PasswordResetOtp pr = otpRepository.findFirstByEmailAndOtpAndUsedIsFalseOrderByCreatedAtDesc(email, otp)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_ACCEPTABLE, "OTP không hợp lệ"));

        if (pr.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ServiceException(HttpStatus.GONE, "OTP đã hết hạn");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Email không tồn tại"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        pr.setUsed(true);
        otpRepository.save(pr);
    }
}
