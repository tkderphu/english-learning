package site.viosmash.english.service;


import org.springframework.stereotype.Service;
import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;

import java.util.List;

@Service
public class AiPromptBuilderServiceImpl implements AiPromptBuilderService {

    @Override
    public String buildRoleplaySystemPrompt(AiChatSession session) {
        String base = session.getSystemPromptSnapshot() != null
                ? session.getSystemPromptSnapshot()
                : session.getInstructionSnapshot();

        return """
                You are an AI English conversation partner in a language learning app.
                Stay fully in character.
                Speak only in English.
                Keep responses concise, natural, and suitable for the learner.
                Ask at most one follow-up question at a time.
                
                Scenario role: %s
                Scenario instruction: %s
                
                Context prompt:
                %s
                """.formatted(
                safe(session.getAiRoleSnapshot()),
                safe(session.getInstructionSnapshot()),
                safe(base)
        );
    }

    @Override
    public String buildRoleplayUserPrompt(List<AiChatMessage> recentMessages, String latestUserMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("Recent conversation:\n");

        for (AiChatMessage m : recentMessages) {
            sb.append(m.getSenderType()).append(": ").append(m.getContent()).append("\n");
        }

        sb.append("\nLatest learner message:\n").append(latestUserMessage).append("\n");
        sb.append("\nRespond as the AI role only.");
        return sb.toString();
    }

    @Override
    public String buildFeedbackPrompt(AiChatSession session, String userMessage) {
        return """
                Analyze the learner's English message for an English learning app.

                Learner message:
                %s

                Return JSON only with this schema:
                {
                  "grammarScore": 0-10,
                  "pronunciationScore": 0-10,
                  "vocabularyScore": 0-10,
                  "fluencyScore": 0-10,
                  "overallComment": "string",
                  "improvedVersion": "string",
                  "naturalSuggestion": "string",
                  "errorCount": 0,
                  "errors": [
                    {
                      "type": "GRAMMAR|VOCABULARY|EXPRESSION|PRONUNCIATION",
                      "originalText": "string",
                      "suggestedText": "string",
                      "explanation": "string"
                    }
                  ]
                }
                """.formatted(userMessage);
    }

    @Override
    public String buildSummaryPrompt(AiChatSession session, List<AiChatMessage> messages) {
        StringBuilder transcript = new StringBuilder();
        for (AiChatMessage m : messages) {
            transcript.append(m.getSenderType()).append(": ").append(m.getContent()).append("\n");
        }

        return """
                Summarize this English speaking practice session.

                Transcript:
                %s

                Return JSON only with:
                {
                  "fluencyLevel": "GOOD|FAIR|NEED_IMPROVEMENT",
                  "grammarLevel": "GOOD|FAIR|NEED_IMPROVEMENT",
                  "vocabularyLevel": "GOOD|FAIR|NEED_IMPROVEMENT",
                  "sentenceCount": 0,
                  "errorCount": 0,
                  "nextSuggestions": ["string", "string", "string"],
                  "nextSuggestion": "string",
                  "relatedTopics": "string"
                }
                """.formatted(transcript);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}