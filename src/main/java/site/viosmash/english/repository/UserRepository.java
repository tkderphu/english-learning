package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.viosmash.english.dto.response.UserResponse;
import site.viosmash.english.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("""
         SELECT new site.viosmash.english.dto.response.UserResponse(
            u.id, u.email, u.fullName, u.avatar, u.role, u.status
         )
         FROM User u
         WHERE (LOWER(u.fullName) LIKE :keyword
                  OR LOWER(u.email) LIKE :keyword)
            AND (:role IS NULL OR u.role = :role)
            AND (:status IS NULL OR u.status = :status)
    """)
    Page<UserResponse> findAllByKeyword(Pageable pageable, String keyword, Integer role, Integer status);
}