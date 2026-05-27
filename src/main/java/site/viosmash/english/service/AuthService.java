package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.LoginRequest;
import site.viosmash.english.dto.response.AuthResponse;
import site.viosmash.english.entity.User;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.UserRepository;
import site.viosmash.english.util.enums.JwtClaims;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AuthService – Xử lý logic xác thực người dùng và tạo JWT Token.
 *
 * <p>Luồng đăng nhập:
 * <ol>
 *   <li>Tìm user theo email trong CSDL qua {@link UserRepository#findByEmail(String)}.</li>
 *   <li>So khớp mật khẩu bằng {@code PasswordEncoder.matches()} (BCrypt).</li>
 *   <li>Sinh {@code refreshToken} (chứa {@code idToken}, {@code expired}, {@code sub}).</li>
 *   <li>Sinh {@code accessToken} (chứa đầy đủ claim: email, role, fullName, refreshToken).</li>
 *   <li>Trả về {@link AuthResponse} gồm cả hai token và thông tin user.</li>
 * </ol>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Value("${spring.security.jwt.access-token.minutes}")
    private int accessToken;

    @Value("${spring.security.jwt.refresh-token.minutes}")
    private int refreshToken;

    /**
     * Thực hiện xác thực người dùng và trả về cặp JWT token.
     *
     * <p>Nếu email không tồn tại hoặc mật khẩu sai, ném {@code 404 Not Found}
     * với thông báo chung để tránh lộ thông tin (email enumeration).</p>
     *
     * @param loginRequest DTO chứa {@code email} và {@code password}
     * @return {@link AuthResponse} với {@code accessToken}, {@code refreshToken}
     *         và thông tin cơ bản của người dùng
     */
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Email hoặc mật khẩu không hợp lệ, vui lòng nhập lại."));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Email hoặc mật khẩu không hợp lệ, vui lòng nhập lại.");
        }

        String refreshToken = buildRefreshToken(user);
        String accessToken = buildAccessToken(user, refreshToken);

        return new AuthResponse(accessToken, refreshToken, (Long) jwtService.extractClaim(JwtClaims.EXPIRED, accessToken), user);
    }

    /**
     * Tạo Refresh Token cho người dùng.
     *
     * <p>Refresh Token chứa các claim: {@code idToken} (UUID ngẫu nhiên),
     * {@code expired} (timestamp hết hạn), {@code sub} (userId).
     * Token được ký bằng HS256 với secret key cấu hình trong {@code application.yml}.</p>
     *
     * @param user Đối tượng User đã xác thực
     * @return Chuỗi JWT Refresh Token
     */
    private String buildRefreshToken(User user) {
        String idToken = UUID.randomUUID().toString();
        Map<String, Object> claimsProperties = new HashMap<>();
        claimsProperties.put(JwtClaims.ID_TOKEN, idToken);
        claimsProperties.put(JwtClaims.EXPIRED, System.currentTimeMillis() + refreshToken * 60 * 1000L);
        claimsProperties.put(JwtClaims.SUB, user.getId());
        return this.jwtService.generateToken(claimsProperties);
    }

    /**
     * Tạo Access Token cho người dùng.
     *
     * <p>Access Token chứa đầy đủ claim cần thiết để phân quyền:
     * {@code email}, {@code preferredName} (họ tên), {@code roleType},
     * {@code expired}, {@code sub} (userId) và {@code refreshToken} (để server
     * có thể thu hồi khi cần). Token có thời hạn ngắn hơn Refresh Token.</p>
     *
     * @param user         Đối tượng User đã xác thực
     * @param refreshToken Refresh Token đã được tạo trước đó (nhúng vào claim)
     * @return Chuỗi JWT Access Token
     */
    private String buildAccessToken(User user, String refreshToken) {

        String idToken = UUID.randomUUID().toString();

        Map<String, Object> claimsProperties = new HashMap<>();
        claimsProperties.put(JwtClaims.ID_TOKEN, idToken);
        claimsProperties.put(JwtClaims.EMAIL, user.getEmail());
        claimsProperties.put(JwtClaims.EXPIRED, System.currentTimeMillis() + accessToken * 60 * 1000L);
        claimsProperties.put(JwtClaims.SUB, user.getId());
        claimsProperties.put(JwtClaims.PREFERRED_NAME, user.getFullName());
        claimsProperties.put(JwtClaims.REFRESH_TOKEN, refreshToken);
        claimsProperties.put(JwtClaims.ROLE_TYPE, user.getRole());
        return this.jwtService.generateToken(claimsProperties);
    }
}