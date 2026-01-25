package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.viosmash.english.dto.response.ProgramResponse;
import site.viosmash.english.entity.Program;

public interface ProgramRepository extends JpaRepository<Program, Integer> {

    @Query("""
        SELECT new site.viosmash.english.dto.response.ProgramResponse(
            p.id, p.title, p.order, p.status
        )
        FROM Program p
        WHERE (:status IS NULL OR p.status = :status)
            AND (LOWER(p.title) LIKE :keyword)
        ORDER BY p.order
    """)
    Page<ProgramResponse> findAllByKeyword(Pageable pageable, String keyword, Integer status);
}
