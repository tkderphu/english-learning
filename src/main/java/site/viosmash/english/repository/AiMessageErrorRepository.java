package site.viosmash.english.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.AiMessageError;

import java.util.List;

public interface AiMessageErrorRepository extends JpaRepository<AiMessageError, Integer> {

    List<AiMessageError> findByFeedbackId(Integer feedbackId);
}