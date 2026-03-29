package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.Sentence;

public interface SentenceRepository extends JpaRepository<Sentence, Integer> {
}
