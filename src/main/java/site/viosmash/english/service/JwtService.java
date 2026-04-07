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

@Service
public class JwtService {

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    public String generateToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .addClaims(claims)
                // Force HS256 so we don't depend on HS512 key length.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public Boolean isExpired(String token) {
        Long expiredAt = (Long) extractClaim(JwtClaims.EXPIRED, token);
        return System.currentTimeMillis() > expiredAt;
    }


    public Object extractClaim(String claim, String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(claim);
    }

    private SecretKey getSigningKey() {
        // HS512 yêu cầu key >= 512 bits. Secret-key có thể đang ngắn -> gây WeakKeyException.
        // Ta chuẩn hóa key thành 64 bytes (SHA-512) nếu cần.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        if (keyBytes.length < 64) {
            keyBytes = sha512(keyBytes);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] sha512(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(input);
        } catch (Exception e) {
            throw new RuntimeException("Cannot compute SHA-512 for JWT signing key", e);
        }
    }
}