package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.PasswordResetOtp;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Integer> {
    Optional<PasswordResetOtp> findFirstByEmailAndOtpAndUsedIsFalseOrderByCreatedAtDesc(String email, String otp);
    void deleteAllByEmail(String email);
}
