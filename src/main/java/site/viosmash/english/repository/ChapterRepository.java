package site.viosmash.english.repository;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.viosmash.english.dto.response.ChapterResponse;
import site.viosmash.english.entity.Chapter;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    @Query(value = """
    SELECT new site.viosmash.english.dto.response.ChapterResponse(
        c.id, c.bookId, c.title, c.description, c.number,
        (SELECT COUNT(p.id) FROM Page p WHERE p.chapterId = c.id),
        (
            SELECT SUM(COALESCE(a.duration, 0))
            FROM Page p
            LEFT JOIN Audio a ON p.audioId = a.id
            WHERE p.chapterId = c.id
        )
    )
    FROM Chapter c
    WHERE c.bookId = :bookId
""", countQuery = "SELECT COUNT(c) FROM Chapter c WHERE c.bookId = :bookId")
    org.springframework.data.domain.Page<ChapterResponse> findByBookIdPaginated(@Param("bookId") int bookId, org.springframework.data.domain.Pageable pageable);

    List<ChapterResponse> findAllByBookId(@Param("id") int id);
}