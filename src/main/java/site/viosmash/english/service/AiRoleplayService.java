package site.viosmash.english.service;



import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;

import java.util.List;

public interface AiRoleplayService {
    String generateReply(AiChatSession session, List<AiChatMessage> recentMessages, String userMessage);

    /** Greeting / scene-setter before the learner sends anything. */
    String generateOpeningLine(AiChatSession session);

    String generateFeedbackJson(AiChatSession session, String userMessage, String inputType);

    String generateSummaryJson(AiChatSession session, List<AiChatMessage> messages);
}