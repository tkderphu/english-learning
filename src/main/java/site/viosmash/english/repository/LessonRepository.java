package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
}
