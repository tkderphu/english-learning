package site.viosmash.english.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.AiChatSession;

import java.util.List;

public interface AiChatSessionRepository extends JpaRepository<AiChatSession, Integer> {

    List<AiChatSession> findByUserIdOrderByCreatedAtDesc(Integer userId);
}