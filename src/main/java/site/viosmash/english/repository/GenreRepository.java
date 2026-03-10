package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.viosmash.english.dto.response.GenreResponse;
import site.viosmash.english.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    @Query("""
         SELECT new site.viosmash.english.dto.response.GenreResponse(
            g.id, g.name, g.thumbnail, g.description, g.status
         )
         FROM Genre g
         WHERE (:keyword IS NULL OR LOWER(g.name) LIKE :keyword)
    """)
    Page<GenreResponse> findAllByKeyword(Pageable pageable, String keyword);
}
