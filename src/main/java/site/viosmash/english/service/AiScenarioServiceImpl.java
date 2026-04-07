package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.response.AiScenarioResponse;
import site.viosmash.english.entity.AiScenario;
import site.viosmash.english.entity.Level;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.AiScenarioRepository;
import site.viosmash.english.repository.LevelRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiScenarioServiceImpl implements AiScenarioService {

    private final AiScenarioRepository aiScenarioRepository;
    private final LevelRepository levelRepository;

    @Override
    public List<AiScenarioResponse> list(Integer levelId, Integer topicId, boolean includeFreeChat) {
        List<AiScenario> scenarios;
        if (levelId != null && topicId != null) {
            scenarios = aiScenarioRepository.findByIsActiveTrueAndLevelIdAndTopicIdOrderByIdAsc(levelId, topicId);
        } else if (levelId != null) {
            scenarios = aiScenarioRepository.findByIsActiveTrueAndLevelIdOrderByIdAsc(levelId);
        } else if (topicId != null) {
            scenarios = aiScenarioRepository.findByIsActiveTrueAndTopicIdOrderByIdAsc(topicId);
        } else {
            scenarios = aiScenarioRepository.findByIsActiveTrueOrderByIdAsc();
        }

        List<AiScenarioResponse> res = new ArrayList<>();
        if (includeFreeChat) {
            res.add(AiScenarioResponse.builder()
                    .id(0)
                    .title("Trò chuyện tự do")
                    .description("Không theo kịch bản, luyện hội thoại tự nhiên với AI.")
                    .topicId(null)
                    .levelId(null)
                    .levelName("FREE")
                    .type("FREE_CHAT")
                    .aiRole("English partner")
                    .instruction("Have a natural English conversation.")
                    .iconUrl(null)
                    .build());
        }

        for (AiScenario s : scenarios) {
            Level lvl = null;
            if (s.getLevelId() != null) {
                int lvlId = s.getLevelId();
                lvl = levelRepository.findById(lvlId)
                        .orElse(null);
            }

            res.add(AiScenarioResponse.builder()
                    .id(s.getId())
                    .title(s.getTitle())
                    .description(s.getDescription())
                    .topicId(s.getTopicId())
                    .levelId(s.getLevelId())
                    .levelName(lvl != null ? lvl.getName() : null)
                    .type(s.getType())
                    .aiRole(s.getAiRole())
                    .instruction(s.getInstruction() != null ? s.getInstruction() : s.getSystemPrompt())
                    .iconUrl(s.getIconUrl())
                    .build());
        }

        return res;
    }

    @Override
    public AiScenarioResponse getDetail(Integer scenarioId) {
        if (scenarioId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "scenarioId is required");
        }

        if (scenarioId == 0) {
            return AiScenarioResponse.builder()
                    .id(0)
                    .title("Trò chuyện tự do")
                    .description("Không theo kịch bản, luyện hội thoại tự nhiên với AI.")
                    .topicId(null)
                    .levelId(null)
                    .levelName("FREE")
                    .type("FREE_CHAT")
                    .aiRole("English partner")
                    .instruction("Have a natural English conversation.")
                    .iconUrl(null)
                    .build();
        }

        AiScenario s = aiScenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Scenario not found"));

        Level lvl = null;
        if (s.getLevelId() != null) {
            int lvlId = s.getLevelId();
            lvl = levelRepository.findById(lvlId).orElse(null);
        }

        return AiScenarioResponse.builder()
                .id(s.getId())
                .title(s.getTitle())
                .description(s.getDescription())
                .topicId(s.getTopicId())
                .levelId(s.getLevelId())
                .levelName(lvl != null ? lvl.getName() : null)
                .type(s.getType())
                .aiRole(s.getAiRole())
                .instruction(s.getInstruction() != null ? s.getInstruction() : s.getSystemPrompt())
                .iconUrl(s.getIconUrl())
                .build();
    }
}

