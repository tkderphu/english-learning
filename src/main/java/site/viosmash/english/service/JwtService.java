package site.viosmash.english.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.viosmash.english.util.enums.JwtClaims;

import javax.crypto.SecretKey;
import java.util.Map;

@Service
public class JwtService {

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    public String generateToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .addClaims(claims)
                .signWith(getSigningKey())
                .compact();
        return token;
    }

    public Boolean isExpired(String token) {
        Long expiredAt = (Long) extractClaim(JwtClaims.EXPIRED, token);
        return System.currentTimeMillis() > expiredAt;
    }


    public Object extractClaim(String claim, String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(claim);
    }


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}