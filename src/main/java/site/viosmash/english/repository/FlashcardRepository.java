package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.viosmash.english.entity.Flashcard;

import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    
    List<Flashcard> findByDeckIdAndStatus(int deckId, int status);
}