package site.viosmash.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.AiChatMessage;

import java.util.List;

public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, Integer> {

    List<AiChatMessage> findBySessionIdOrderByCreatedAtAsc(Integer sessionId);

    long countBySessionIdAndSenderType(Integer sessionId, String senderType);
}