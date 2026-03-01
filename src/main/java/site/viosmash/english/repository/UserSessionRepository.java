package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.viosmash.english.dto.response.UserSessionResponse;
import site.viosmash.english.entity.UserSession;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {

    Optional<UserSession> findByRefreshTokenAndStatus(String refreshToken, int status);

    @Query("""
        SELECT new site.viosmash.english.dto.response.UserSessionResponse(
            u.id, u.fullName, u.email, u.avatar, us.createdAt, us.remoteIp, us.userAgent
        )
        FROM UserSession us
            JOIN User u ON us.userId = u.id
        WHERE u.status = 1
            AND LOWER(u.email) LIKE :keyword
            AND (:userId IS NULL OR us.userId = :userId)
            AND (:status IS NULL OR us.status = :status)
    """)
    Page<UserSessionResponse> findAllByKeyword(Pageable pageable, @Param("keyword") String keyword, @Param("userId") Integer userId, @Param("status") Integer status);
}