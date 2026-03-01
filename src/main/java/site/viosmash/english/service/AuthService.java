package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.LoginRequest;
import site.viosmash.english.dto.response.AuthResponse;
import site.viosmash.english.entity.User;
import site.viosmash.english.entity.UserSession;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.UserRepository;
import site.viosmash.english.repository.UserSessionRepository;
import site.viosmash.english.util.enums.JwtClaims;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final UserSessionRepository userSessionRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Value("${spring.security.jwt.access-token.minutes}")
    private int accessToken;

    @Value("${spring.security.jwt.refresh-token.minutes}")
    private int refreshToken;

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Email không hợp lệ, vui lòng nhập lại."));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ServiceException(HttpStatus.NOT_ACCEPTABLE, "Mật khẩu không hợp lệ, vui lòng nhập lại.");
        }

        String refreshToken = buildRefreshToken(user);
        String accessToken = buildAccessToken(user, refreshToken);

        UserSession session = new UserSession()
                .setUserId(user.getId())
                .setRefreshToken(refreshToken);
        session.setStatus(1);

        this.userSessionRepository.save(session);

        return new AuthResponse(accessToken, refreshToken, user);
    }

    private String buildRefreshToken(User user) {
        String idToken = UUID.randomUUID().toString();
        Map<String, Object> claimsProperties = new HashMap<>();
        claimsProperties.put(JwtClaims.ID_TOKEN, idToken);
        claimsProperties.put(JwtClaims.EXPIRED, System.currentTimeMillis() + refreshToken * 60 * 1000);
        claimsProperties.put(JwtClaims.SUB, user.getId());
        return this.jwtService.generateToken(claimsProperties);
    }

    private String buildAccessToken(User user, String refreshToken) {

        String idToken = UUID.randomUUID().toString();

        Map<String, Object> claimsProperties = new HashMap<>();
        claimsProperties.put(JwtClaims.ID_TOKEN, idToken);
        claimsProperties.put(JwtClaims.EMAIL, user.getEmail());
        claimsProperties.put(JwtClaims.EXPIRED, System.currentTimeMillis() + accessToken * 60 * 1000);
        claimsProperties.put(JwtClaims.SUB, user.getId());
        claimsProperties.put(JwtClaims.PREFERRED_NAME, user.getFullName());
        claimsProperties.put(JwtClaims.REFRESH_TOKEN, refreshToken);
        claimsProperties.put(JwtClaims.ROLE_TYPE, user.getRole());
        return this.jwtService.generateToken(claimsProperties);
    }
}