package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.viosmash.english.dto.response.AiScenarioResponse;
import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.AiScenarioService;

import java.util.List;

@RestController
@RequestMapping("/api/ai-chat")
@RequiredArgsConstructor
@Tag(name = "AI Scenarios", description = "AI scenario management")
public class AiScenarioController {

    private final AiScenarioService aiScenarioService;

    @Operation(summary = "Get available AI chat scenarios")
    @GetMapping("/v1/scenarios")
    public ResponseEntity<BaseResponse<List<AiScenarioResponse>>> list(
            @RequestParam(required = false) Integer levelId,
            @RequestParam(required = false) Integer topicId,
            @RequestParam(defaultValue = "true") boolean includeFreeChat
    ) {
        List<AiScenarioResponse> res = aiScenarioService.list(levelId, topicId, includeFreeChat);
        return ResponseEntity.ok(BaseResponse.success(res));
    }

    @Operation(summary = "Get scenario detail by id")
    @GetMapping("/v1/scenarios/{scenarioId}")
    public ResponseEntity<BaseResponse<AiScenarioResponse>> detail(@PathVariable Integer scenarioId) {
        return ResponseEntity.ok(BaseResponse.success(aiScenarioService.getDetail(scenarioId)));
    }
}

