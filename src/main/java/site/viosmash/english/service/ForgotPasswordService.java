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

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final PasswordResetOtpRepository otpRepository;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.otp.ttl.minutes:5}")
    private int otpTtlMinutes;

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

    private String generateOtp() {
        Random r = new Random();
        int number = 100000 + r.nextInt(900000);
        return String.valueOf(number);
    }

    public void verifyOtp(String email, String otp) {
        PasswordResetOtp pr = otpRepository.findFirstByEmailAndOtpAndUsedIsFalseOrderByCreatedAtDesc(email, otp)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_ACCEPTABLE, "OTP không hợp lệ"));
        if (pr.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ServiceException(HttpStatus.GONE, "OTP đã hết hạn");
        }
    }

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
