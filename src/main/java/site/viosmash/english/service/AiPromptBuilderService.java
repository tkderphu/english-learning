package site.viosmash.english.service;



import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;

import java.util.List;

public interface AiPromptBuilderService {
    String buildRoleplaySystemPrompt(AiChatSession session);
    String buildRoleplayUserPrompt(List<AiChatMessage> recentMessages, String latestUserMessage);
    String buildFeedbackPrompt(AiChatSession session, String userMessage);
    String buildSummaryPrompt(AiChatSession session, List<AiChatMessage> messages);
}