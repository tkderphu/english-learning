package site.viosmash.english.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.viosmash.english.dto.response.BookPageResponse;
import site.viosmash.english.entity.Page;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Integer> {

    @Query("""
        SELECT new site.viosmash.english.dto.response.BookPageResponse(
            p.id, p.number,
            new site.viosmash.english.dto.response.AudioResponse(
                a.id, a.duration, a.format, a.sampleRate, a.fileSize, a.fileUrl, p.id
            )
        )
        FROM Page p
            LEFT JOIN Audio a ON p.audioId = a.id
        WHERE p.chapterId IN (SELECT c.id FROM Chapter c WHERE c.bookId = :bookId)
        ORDER BY p.number ASC
    """)
    List<BookPageResponse> findByBookId(Pageable pageable, @Param("bookId") int bookId);
}