package site.viosmash.english.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import site.viosmash.english.entity.AiScenario;

import java.util.List;

public interface AiScenarioRepository extends JpaRepository<AiScenario, Integer> {

    List<AiScenario> findByIsActiveTrueOrderByIdAsc();

    List<AiScenario> findByIsActiveTrueAndLevelIdOrderByIdAsc(Integer levelId);

    List<AiScenario> findByIsActiveTrueAndTopicIdOrderByIdAsc(Integer topicId);

    List<AiScenario> findByIsActiveTrueAndLevelIdAndTopicIdOrderByIdAsc(Integer levelId, Integer topicId);
}
