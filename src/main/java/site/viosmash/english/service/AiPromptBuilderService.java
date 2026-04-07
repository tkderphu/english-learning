package site.viosmash.english.service;



import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;

import java.util.List;

public interface AiPromptBuilderService {
    String buildRoleplaySystemPrompt(AiChatSession session);
    String buildRoleplayUserPrompt(AiChatSession session, List<AiChatMessage> recentMessages, String latestUserMessage);
    /** First line of the session before the learner has spoken (AI speaks first). */
    String buildOpeningUserPrompt(AiChatSession session);
    String buildFeedbackPrompt(AiChatSession session, String userMessage, String inputType);
    String buildSummaryPrompt(AiChatSession session, List<AiChatMessage> messages);
}