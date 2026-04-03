package site.viosmash.english.repository;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.viosmash.english.dto.response.ChapterResponse;
import site.viosmash.english.entity.Chapter;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    @Query("""
    SELECT new site.viosmash.english.dto.response.ChapterResponse(
        c.id, b.id, c.title, c.description, c.number,
        (SELECT COUNT(p.id) FROM Page p WHERE p.chapterId = c.id),
        (
            SELECT SUM(COALESCE(a.duration, 0))
            FROM Page p
            LEFT JOIN Audio a ON p.audioId = a.id
            WHERE p.chapterId = c.id
        )
    )
    FROM Chapter c
    JOIN Book b ON c.bookId = b.id
    WHERE b.id = :id
""")
    List<ChapterResponse> findAllByBookId(@Param("id") int id);
}