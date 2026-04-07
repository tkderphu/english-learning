package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.viosmash.english.dto.response.AuthorResponse;
import site.viosmash.english.entity.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("""
         SELECT new site.viosmash.english.dto.response.AuthorResponse(
            a.id, a.name, a.avatar, a.nationality, a.biography, a.status
         )
         FROM Author a
         WHERE (:keyword IS NULL OR LOWER(a.name) LIKE :keyword)
    """)
    Page<AuthorResponse> findAllByKeyword(Pageable pageable, String keyword);

    @Query("""
         SELECT new site.viosmash.english.dto.response.AuthorResponse(
            a.id, a.name, a.avatar, a.nationality, a.biography, a.status
         )
         FROM Author a
         WHERE a.status = 1
         ORDER BY a.id
    """)
    List<AuthorResponse> findAllActive();

    @Query("""
         SELECT new site.viosmash.english.dto.response.AuthorResponse(
            a.id, a.name, a.avatar, a.nationality, a.biography, a.status
         )
         FROM Author a
         WHERE a.status = 1
         ORDER BY a.id
    """)
    Page<AuthorResponse> findAllActive(Pageable pageable);
}
