package site.viosmash.english.service;

import site.viosmash.english.dto.response.AiScenarioResponse;

import java.util.List;

public interface AiScenarioService {
    List<AiScenarioResponse> list(Integer levelId, Integer topicId, boolean includeFreeChat);

    AiScenarioResponse getDetail(Integer scenarioId);
}

