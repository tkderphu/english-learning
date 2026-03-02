package site.viosmash.english.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.viosmash.english.entity.User;
import site.viosmash.english.service.JwtService;
import site.viosmash.english.util.enums.JwtClaims;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        Long tokenExpiredAt = (Long) jwtService.extractClaim(JwtClaims.EXPIRED, token);

        if (System.currentTimeMillis() <= tokenExpiredAt) {

            User user = new User();
            user.setId((Integer) jwtService.extractClaim(JwtClaims.SUB, token));
            user.setEmail((String) jwtService.extractClaim(JwtClaims.PREFERRED_NAME, token));
            user.setRole((Integer) jwtService.extractClaim(JwtClaims.ROLE_TYPE, token));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, Collections.emptyList());

            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}