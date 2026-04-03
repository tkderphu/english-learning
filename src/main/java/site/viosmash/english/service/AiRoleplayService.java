package site.viosmash.english.service;



import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;

import java.util.List;

public interface AiRoleplayService {
    String generateReply(AiChatSession session, List<AiChatMessage> recentMessages, String userMessage);

    String generateFeedbackJson(AiChatSession session, String userMessage);

    String generateSummaryJson(AiChatSession session, List<AiChatMessage> messages);
}