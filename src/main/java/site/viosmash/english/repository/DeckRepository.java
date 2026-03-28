package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.viosmash.english.entity.Deck;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer> {
    
    List<Deck> findByStatus(int status);

    List<Deck> findByUserIdAndStatus(int userId, int status);
}