package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.viosmash.english.entity.Deck;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer> {
    
    List<Deck> findByStatus(int status);

    @org.springframework.data.jpa.repository.Query("SELECT d FROM Deck d WHERE d.user.id = :userId AND d.status = :status AND (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    List<Deck> findByUserIdAndStatusAndTitleContainingIgnoreCase(@org.springframework.data.repository.query.Param("userId") int userId, @org.springframework.data.repository.query.Param("status") int status, @org.springframework.data.repository.query.Param("title") String title);
}