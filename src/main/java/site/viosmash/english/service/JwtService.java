package site.viosmash.english.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.viosmash.english.util.enums.JwtClaims;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.util.Map;

/**
 * JwtService – Dịch vụ tạo và xác minh JWT Token.
 *
 * <p>Sử dụng thư viện JJWT với thuật toán ký {@code HS256}. Secret key được
 * đọc từ biến môi trường {@code JWT_SECRET} (cấu hình qua {@code spring.security.jwt.secret-key}).
 * Nếu key ngắn hơn 512-bit, tự động mở rộng bằng SHA-512 để đảm bảo độ an toàn.</p>
 */
@Service
public class JwtService {

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Tạo JWT Token từ danh sách claim tuỳ chỉnh.
     *
     * <p>Ký token bằng {@code HS256} với key được derive từ {@code secret-key}.
     * Các claim thường bao gồm: {@code sub} (userId), {@code email},
     * {@code roleType}, {@code expired} (timestamp tự quản lý), {@code refreshToken}.</p>
     *
     * @param claims Map chứa các claim muốn đính kèm vào token
     * @return Chuỗi JWT đã ký (compact form)
     */
    public String generateToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .addClaims(claims)
                // Force HS256 so we don't depend on HS512 key length.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    /**
     * Kiểm tra JWT Token có hết hạn hay chưa.
     *
     * <p>Đọc claim {@code expired} (timestamp dạng long ms) từ token và
     * so sánh với thời điểm hiện tại.</p>
     *
     * @param token Chuỗi JWT cần kiểm tra
     * @return {@code true} nếu token đã hết hạn, {@code false} nếu còn hiệu lực
     */
    public Boolean isExpired(String token) {
        Long expiredAt = (Long) extractClaim(JwtClaims.EXPIRED, token);
        return System.currentTimeMillis() > expiredAt;
    }

    /**
     * Trích xuất giá trị của một claim cụ thể từ JWT Token.
     *
     * <p>Giải mã và xác minh chữ ký token trước khi đọc claim.
     * Nếu token không hợp lệ hoặc bị giả mạo, JJWT sẽ ném exception.</p>
     *
     * @param claim Tên claim cần lấy (ví dụ: {@link JwtClaims#SUB}, {@link JwtClaims#EMAIL})
     * @param token Chuỗi JWT
     * @return Giá trị của claim, hoặc {@code null} nếu claim không tồn tại
     */
    public Object extractClaim(String claim, String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(claim);
    }

    /**
     * Lấy signing key đã được chuẩn hoá về 512-bit.
     *
     * <p>HS512 yêu cầu key &ge; 512 bits. Nếu secret-key base64 ngắn hơn 64 bytes,
     * tự động mở rộng bằng SHA-512 hash để tránh {@code WeakKeyException}.
     * Sử dụng HS256 khi ký để không phụ thuộc vào độ dài key.</p>
     *
     * @return {@link SecretKey} sẵn sàng dùng để ký/xác minh JWT
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        if (keyBytes.length < 64) {
            keyBytes = sha512(keyBytes);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Mở rộng key ngắn bằng thuật toán SHA-512 (64 bytes output).
     *
     * @param input Mảng byte đầu vào cần hash
     * @return Mảng byte 64 phần tử sau khi SHA-512
     */
    private byte[] sha512(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(input);
        } catch (Exception e) {
            throw new RuntimeException("Cannot compute SHA-512 for JWT signing key", e);
        }
    }
}