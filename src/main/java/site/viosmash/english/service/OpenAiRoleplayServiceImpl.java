package site.viosmash.english.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.OpenAiTextRequest;
import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;
import site.viosmash.english.groqapi.GroqAiClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiRoleplayServiceImpl implements AiRoleplayService {

    private final GroqAiClient groqAiClient;
    private final AiPromptBuilderService aiPromptBuilderService;

    @Value("${openai.model.roleplay}")
    private String roleplayModel;

    @Value("${openai.model.feedback}")
    private String feedbackModel;

    @Override
    public String generateReply(AiChatSession session, List<AiChatMessage> recentMessages, String userMessage) {
        String systemPrompt = aiPromptBuilderService.buildRoleplaySystemPrompt(session);
        String userPrompt = aiPromptBuilderService.buildRoleplayUserPrompt(session, recentMessages, userMessage);

        OpenAiTextRequest request = OpenAiTextRequest.builder()
                .model(roleplayModel)
                .input(List.of(
                        Map.of(
                                "role", "system",
                                "content", List.of(Map.of("type", "input_text", "text", systemPrompt))
                        ),
                        Map.of(
                                "role", "user",
                                "content", List.of(Map.of("type", "input_text", "text", userPrompt))
                        )
                ))
                .build();

        return groqAiClient.createTextResponse(request);
    }

    @Override
    public String generateOpeningLine(AiChatSession session) {
        String systemPrompt = aiPromptBuilderService.buildRoleplaySystemPrompt(session);
        String userPrompt = aiPromptBuilderService.buildOpeningUserPrompt(session);

        OpenAiTextRequest request = OpenAiTextRequest.builder()
                .model(roleplayModel)
                .input(List.of(
                        Map.of(
                                "role", "system",
                                "content", List.of(Map.of("type", "input_text", "text", systemPrompt))
                        ),
                        Map.of(
                                "role", "user",
                                "content", List.of(Map.of("type", "input_text", "text", userPrompt))
                        )
                ))
                .build();

        return groqAiClient.createTextResponse(request);
    }

    @Override
    public String generateFeedbackJson(AiChatSession session, String userMessage, String inputType) {
        String prompt = aiPromptBuilderService.buildFeedbackPrompt(session, userMessage, inputType);
        boolean voice = inputType != null && inputType.equalsIgnoreCase("VOICE");
        String system = voice
                ? "You are an English evaluator. The learner spoke and the text is a speech-to-text transcript. Score pronunciation (from the transcript as proxy for clarity and spoken form) plus grammar. Return strict JSON only."
                : "You are an English evaluator. The learner typed this message (not spoken). Score grammar only: set pronunciationScore, vocabularyScore, and fluencyScore to null. Return strict JSON only.";

        OpenAiTextRequest request = OpenAiTextRequest.builder()
                .model(feedbackModel)
                .input(List.of(
                        Map.of(
                                "role", "system",
                                "content", List.of(Map.of("type", "input_text", "text", system))
                        ),
                        Map.of(
                                "role", "user",
                                "content", List.of(Map.of("type", "input_text", "text", prompt))
                        )
                ))
                .build();

        return groqAiClient.createTextResponse(request);
    }

    @Override
    public String generateSummaryJson(AiChatSession session, List<AiChatMessage> messages) {
        String prompt = aiPromptBuilderService.buildSummaryPrompt(session, messages);

        OpenAiTextRequest request = OpenAiTextRequest.builder()
                .model(feedbackModel)
                .input(List.of(
                        Map.of(
                                "role", "system",
                                "content", List.of(Map.of("type", "input_text", "text", "You are an English learning assessor. Return strict JSON only."))
                        ),
                        Map.of(
                                "role", "user",
                                "content", List.of(Map.of("type", "input_text", "text", prompt))
                        )
                ))
                .build();

        return groqAiClient.createTextResponse(request);
    }
}