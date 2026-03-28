package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.AiMessageFeedback;

import java.util.Optional;

public interface AiMessageFeedbackRepository extends JpaRepository<AiMessageFeedback, Integer> {

    Optional<AiMessageFeedback> findByMessageId(Integer messageId);
}