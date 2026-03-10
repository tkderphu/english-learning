package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.AuthorBook;

import java.util.List;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, Integer> {
}
