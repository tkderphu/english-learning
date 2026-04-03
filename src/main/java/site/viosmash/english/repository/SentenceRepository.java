package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.viosmash.english.dto.response.BookPageResponse;
import site.viosmash.english.dto.response.SentenceResponse;
import site.viosmash.english.entity.Sentence;

import java.util.List;

public interface SentenceRepository extends JpaRepository<Sentence, Integer> {


    @Query(value = """
        SELECT new site.viosmash.english.dto.response.SentenceResponse(
            p.id, s.id, s.content, s.transcription1, s.startTime, s.endTime
        )
        FROM Sentence s
            JOIN Page p ON s.pageId = p.id
        WHERE p.id = :pageId
    """)

    List<SentenceResponse> findAllByPageId(@Param("pageId") Integer pageId);
}