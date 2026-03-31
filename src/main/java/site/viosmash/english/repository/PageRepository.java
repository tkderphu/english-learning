package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.viosmash.english.entity.Page;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Integer> {

    @Query("SELECT p FROM Page p WHERE p.chapterId IN " +
           "(SELECT c.id FROM Chapter c WHERE c.bookId = :bookId) " +
           "ORDER BY p.number ASC")
    List<Page> findByBookId(@Param("bookId") int bookId, org.springframework.data.domain.Pageable pageable);
}
