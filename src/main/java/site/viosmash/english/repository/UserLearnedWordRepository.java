package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.UserLearnedWord;

import java.util.Optional;

public interface UserLearnedWordRepository extends JpaRepository<UserLearnedWord, Integer> {

    long countByUserId(Integer userId);

    Optional<UserLearnedWord> findByUserIdAndTerm(Integer userId, String term);

    Page<UserLearnedWord> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);

    Page<UserLearnedWord> findByUserIdAndFavoriteTrueOrderByCreatedAtDesc(Integer userId, Pageable pageable);

    Page<UserLearnedWord> findByUserIdAndNeedsAttentionTrueOrderByCreatedAtDesc(Integer userId, Pageable pageable);

    Page<UserLearnedWord> findByUserIdAndTermContainingIgnoreCaseOrderByCreatedAtDesc(Integer userId, String term, Pageable pageable);

    Page<UserLearnedWord> findByUserIdAndFavoriteTrueAndTermContainingIgnoreCaseOrderByCreatedAtDesc(
            Integer userId, String term, Pageable pageable);

    Page<UserLearnedWord> findByUserIdAndNeedsAttentionTrueAndTermContainingIgnoreCaseOrderByCreatedAtDesc(
            Integer userId, String term, Pageable pageable);
}
