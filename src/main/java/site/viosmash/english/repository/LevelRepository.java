package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.viosmash.english.dto.response.LevelResponse;
import site.viosmash.english.entity.Level;

public interface LevelRepository extends JpaRepository<Level, Integer> {

    @Query("""
         SELECT new site.viosmash.english.dto.response.LevelResponse(
            l.id, l.name, l.description, l.numberCourse, l.status
         )
         FROM Level l
         WHERE (:keyword IS NULL OR LOWER(l.name) LIKE :keyword)
    """)
    Page<LevelResponse> findAllByKeyword(Pageable pageable, String keyword);
}
