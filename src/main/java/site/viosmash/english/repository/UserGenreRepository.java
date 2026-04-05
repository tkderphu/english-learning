package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.UserGenre;

import java.util.List;

public interface UserGenreRepository extends JpaRepository<UserGenre, Integer> {
    void deleteByUserId(Integer userId);
    List<UserGenre> findByUserId(Integer userId);
}
