package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.Sentence;

import java.util.List;

public interface SentenceRepository extends JpaRepository<Sentence, Integer> {

    List<Sentence> findByPageIdIn(List<Integer> pageIds);
}
