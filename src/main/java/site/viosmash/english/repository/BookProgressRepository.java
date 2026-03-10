package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.viosmash.english.dto.response.BookResponse;
import site.viosmash.english.entity.BookProgress;

public interface BookProgressRepository extends JpaRepository<BookProgress, Integer> {

}
