package site.viosmash.english.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.viosmash.english.entity.LearningActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface LearningActivityRepository extends JpaRepository<LearningActivity, Integer> {

    boolean existsByUserIdAndReferenceTypeAndReferenceId(Integer userId, String referenceType, Integer referenceId);

    List<LearningActivity> findByUserIdAndStartedAtBetweenOrderByStartedAtAsc(
            Integer userId,
            LocalDateTime startInclusive,
            LocalDateTime endExclusive
    );

    @Query(value = """
            SELECT DATE(started_at) as d,
                   COALESCE(SUM(duration_seconds), 0) as totalSec,
                   COUNT(*) as cnt
            FROM learning_activities
            WHERE user_id = :userId
              AND started_at >= :from
              AND started_at < :to
            GROUP BY DATE(started_at)
            ORDER BY d
            """, nativeQuery = true)
    List<Object[]> aggregateMinutesByDay(
            @Param("userId") Integer userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query(value = """
            SELECT DISTINCT DATE(started_at)
            FROM learning_activities
            WHERE user_id = :userId
            ORDER BY 1 DESC
            """, nativeQuery = true)
    List<java.sql.Date> findDistinctActivityDates(@Param("userId") Integer userId);

    long countByUserIdAndActivityTypeIn(Integer userId, List<String> types);

    @Query(value = """
            SELECT COUNT(DISTINCT DATE(started_at))
            FROM learning_activities
            WHERE user_id = :userId
            """, nativeQuery = true)
    long countDistinctStudyDaysNative(@Param("userId") Integer userId);

    @Query("""
            SELECT a FROM LearningActivity a
            WHERE a.userId = :userId
              AND a.activityType IN :types
              AND (:q IS NULL OR LOWER(COALESCE(a.title, '')) LIKE LOWER(CONCAT('%', :q, '%')))
            """)
    Page<LearningActivity> findHistory(
            @Param("userId") Integer userId,
            @Param("types") List<String> types,
            @Param("q") String q,
            Pageable pageable
    );
}
