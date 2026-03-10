package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.BookGenre;

import java.util.List;

public interface BookGenreRepository extends JpaRepository<BookGenre, Integer> {
}
