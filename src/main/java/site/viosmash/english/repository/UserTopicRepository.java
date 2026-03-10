package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.UserTopic;

import java.util.List;

public interface UserTopicRepository extends JpaRepository<UserTopic, Integer> {
}
