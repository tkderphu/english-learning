package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    Optional<UserProfile> findByUserId(Integer userId);
}
