package site.viosmash.english.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import site.viosmash.english.entity.User;
import site.viosmash.english.service.JwtService;
import site.viosmash.english.util.enums.JwtClaims;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        Long tokenExpiredAt = (Long) jwtService.extractClaim(JwtClaims.EXPIRED, token);

        if(System.currentTimeMillis() <= tokenExpiredAt) {

            User user = new User();
            Object sub = jwtService.extractClaim(JwtClaims.SUB, token);
            Object email = jwtService.extractClaim(JwtClaims.PREFERRED_NAME, token);
            Object role = jwtService.extractClaim(JwtClaims.ROLE_TYPE, token);

            if(sub != null) {
                Integer id = (sub instanceof Integer) ? (Integer) sub : ((Long) sub).intValue();
                user.setId(id);
            }
            if(email != null) user.setEmail(email.toString());
            if(role != null) {
                Integer r = (role instanceof Integer) ? (Integer) role : ((Long) role).intValue();
                user.setRole(r);
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, Collections.emptyList()
            );

            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}