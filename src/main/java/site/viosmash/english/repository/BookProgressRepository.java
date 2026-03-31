package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.BookProgress;

import java.util.Optional;

public interface BookProgressRepository extends JpaRepository<BookProgress, Integer> {
	Optional<BookProgress> findByUserIdAndBookId(int userId, int bookId);
}
