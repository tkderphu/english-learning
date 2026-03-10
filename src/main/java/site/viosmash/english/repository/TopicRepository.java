package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.viosmash.english.dto.response.TopicResponse;
import site.viosmash.english.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query("""
         SELECT new site.viosmash.english.dto.response.TopicResponse(
            t.id, t.name, t.description, t.thumbnail, t.status
         )
         FROM Topic t
         WHERE (:keyword IS NULL OR LOWER(t.name) LIKE :keyword)
    """)
    Page<TopicResponse> findAllByKeyword(Pageable pageable, String keyword);
}
