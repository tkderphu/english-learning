package site.viosmash.english.service;

import org.springframework.stereotype.Service;
import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;

import java.util.List;

@Service
public class AiPromptBuilderServiceImpl implements AiPromptBuilderService {

    private static boolean isScenarioSession(AiChatSession session) {
        if (session.getScenarioId() != null) {
            return true;
        }
        String type = session.getSessionType();
        return type != null && type.equalsIgnoreCase("SCENARIO");
    }

    @Override
    public String buildRoleplaySystemPrompt(AiChatSession session) {
        boolean scenario = isScenarioSession(session);
        String title = safe(session.getTitle());
        String role = safe(session.getAiRoleSnapshot());
        String instruction = safe(session.getInstructionSnapshot());
        String extraFromContent = safe(session.getSystemPromptSnapshot());
        String goalType = safe(session.getGoalType());
        String focusSkill = safe(session.getFocusSkill());
        String coachingMode = safe(session.getCoachingMode());

        String topicRules = scenario
                ? """
                TOPIC LOCK (mandatory for this session):
                - The learner explicitly chose this scenario. Keep every turn relevant to it; stay in the implied setting and vocabulary.
                - Do not switch to unrelated small talk or new topics. If they drift off-topic, acknowledge briefly and steer back toward the scenario goal.
                - Help them practice completing the task in "Scenario goal" (e.g. ordering, asking for help, checking in).
                - The conversation starts with your in-character greeting (already in the transcript or you are generating it now); then respond naturally to whatever they say.
                """
                : """
                FREE CHAT:
                - Natural conversation; you may follow the learner across topics if they lead.
                - The transcript may already contain your first greeting; continue naturally from there.
                """;

        String extraBlock = extraFromContent.isEmpty()
                ? ""
                : "\nAdditional notes from content team:\n" + extraFromContent + "\n";

        String goalModeRules = buildGoalModeRoleplayRules(goalType, focusSkill, coachingMode);

        return """
                You are an AI English conversation partner in a language learning app.
                Speak only in English. Stay fully in character for the role below.
                Keep responses concise, natural, and suitable for the learner's level.
                Ask at most one follow-up question per turn unless a short clarification is essential.

                LEARNER INPUT (critical — do not reinforce mistakes):
                - Their lines may be speech-to-text: wrong words, mixed languages, nonsense syllables, or sounds misheard as unrelated text. Infer intent from context and the scenario.
                - Reply using only natural, correct English. Never repeat, quote, or play back their broken wording, garbled phrases, or non-English fragments (e.g. do not say back misheard "coffee" as random syllables).
                - If they meant something simple (e.g. ordering coffee), continue with normal vocabulary ("coffee", "a coffee", "your drink") instead of echoing incorrect tokens.
                - You may gently confirm intent in good English ("So you'd like a coffee?") without copying their errors.

                %s

                Session title: %s
                Your in-character role: %s
                Scenario goal (what the learner should practice): %s
                Session learning goal: %s
                Session focus skill: %s
                Session coaching mode: %s
                %s
                %s""".formatted(
                topicRules,
                title.isEmpty() ? "(practice session)" : title,
                role.isEmpty() ? "English practice partner" : role,
                instruction.isEmpty() ? "General English practice." : instruction,
                goalType,
                focusSkill,
                coachingMode,
                goalModeRules,
                extraBlock
        );
    }

    @Override
    public String buildRoleplayUserPrompt(
            AiChatSession session,
            List<AiChatMessage> recentMessages,
            String latestUserMessage,
            List<String> personalizedWords
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("Recent conversation:\n");

        for (AiChatMessage m : recentMessages) {
            sb.append(m.getSenderType()).append(": ").append(m.getContent()).append("\n");
        }

        sb.append("\nLatest learner message:\n").append(latestUserMessage).append("\n");
        sb.append("\n[Quality reminder] Their last line may contain STT or learner errors. Answer in clear correct English; do not reuse their mistaken words or nonsense phrases in your reply.\n");
        if (isScenarioSession(session)) {
            String title = safe(session.getTitle());
            String instruction = safe(session.getInstructionSnapshot());
            sb.append("\n[Turn reminder] Stay in the scenario");
            if (!title.isEmpty()) {
                sb.append(" \"").append(title).append("\"");
            }
            sb.append(". Learner task: ").append(instruction.isEmpty() ? "follow the scene in character." : instruction).append("\n");
        }
        if (personalizedWords != null && !personalizedWords.isEmpty()) {
            sb.append("\n[Personalization] Learner recently studied these words:\n");
            sb.append(String.join(", ", personalizedWords)).append("\n");
            sb.append("""
                    
                    Include at least 1 of these words naturally in this reply when context allows.
                    If none fits naturally, you may skip them to keep the response correct and fluent.
                    Never list the words explicitly.
                    Use the chosen personalized item exactly as provided; do not shorten a phrase to one word
                    (e.g., keep "black coffee" as "black coffee", not just "coffee").
                    If you use a personalized word, wrap that exact word in HTML bold tags as <b>word</b>.
                    Keep bold tags only for personalized words you actually used.
                    Prioritize a natural conversation flow over vocabulary insertion.
                    """);
        }
        sb.append("\nRespond as the AI role only, in English.");
        return sb.toString();
    }

    @Override
    public String buildOpeningUserPrompt(AiChatSession session) {
        boolean scenario = isScenarioSession(session);
        String title = safe(session.getTitle());
        String instruction = safe(session.getInstructionSnapshot());
        String role = safe(session.getAiRoleSnapshot());

        if (scenario) {
            return """
                    The session has just started. The learner has not said anything yet.
                    You must speak first as your character (%s).

                    Write exactly ONE short opening line in English (1–2 sentences):
                    - Greet them in character for this setting.
                    - Briefly set the scene if helpful.
                    - End with a simple invitation that matches the scenario goal so they know how to reply (e.g. ask what they would like).

                    Scenario title: %s
                    Scenario goal for the learner: %s

                    Rules: English only; stay in character; no meta-commentary about being an AI; do not ask them to "start the roleplay" in abstract terms—stay concrete to the scene.
                    """.formatted(
                    role.isEmpty() ? "your assigned role" : role,
                    title.isEmpty() ? "(practice)" : title,
                    instruction.isEmpty() ? "Practice English in this situation." : instruction
            );
        }
        return """
                The session has just started. The learner has not said anything yet.
                Speak first as a friendly English practice partner.

                Write exactly ONE short opening in English (1–2 sentences): warm greeting + one easy question to get them talking.
                English only; no meta-commentary about being an AI.
                """;
    }

    @Override
    public String buildFeedbackPrompt(AiChatSession session, String userMessage, String inputType) {
        String goalType = safe(session.getGoalType());
        String focusSkill = safe(session.getFocusSkill());
        String coachingMode = safe(session.getCoachingMode());
        String feedbackRules = buildGoalModeFeedbackRules(goalType, focusSkill, coachingMode);
        String scenarioLine = "";
        if (isScenarioSession(session)) {
            scenarioLine = """
                    Practice context (scenario the learner chose):
                    Title: %s
                    Goal: %s

                    """.formatted(
                    safe(session.getTitle()).isEmpty() ? "—" : safe(session.getTitle()),
                    safe(session.getInstructionSnapshot()).isEmpty() ? "—" : safe(session.getInstructionSnapshot())
            );
        }
        boolean voice = inputType != null && inputType.equalsIgnoreCase("VOICE");
        if (voice) {
            return """
                    Analyze the learner's English for a speaking-practice app.
                    Input channel: VOICE. The text below is a speech-to-text transcript of what they said.
                    Score grammar, vocabulary, and fluency from the wording; score pronunciation as a reasonable estimate from the transcript (word forms, missing words, obvious mis-recognitions).
                    Language rules for returned text fields:
                    - "improvedVersion" and "suggestedText" MUST be in natural ENGLISH.
                    - "overallComment", "naturalSuggestion", and each "errors[].explanation" MUST be in clear VIETNAMESE.
                    - Keep Vietnamese explanations short, practical, and easy for learners.
                    Session policy:
                    - GoalType: %s
                    - FocusSkill: %s
                    - CoachingMode: %s
                    %s

                    %sTranscript (learner):
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
                    """.formatted(goalType, focusSkill, coachingMode, feedbackRules, scenarioLine, userMessage);
        }
        return """
                Analyze the learner's English for a chat app.
                Input channel: TYPED TEXT (not spoken). Do not score speaking or pronunciation — there is no audio.
                Focus on grammar, word choice, and natural written expression only.
                Language rules for returned text fields:
                - "improvedVersion" and "suggestedText" MUST be in natural ENGLISH.
                - "overallComment", "naturalSuggestion", and each "errors[].explanation" MUST be in clear VIETNAMESE.
                - Keep Vietnamese explanations short, practical, and easy for learners.
                Session policy:
                - GoalType: %s
                - FocusSkill: %s
                - CoachingMode: %s
                %s

                %sLearner message:
                %s

                Return JSON only with this schema (pronunciationScore, vocabularyScore, and fluencyScore MUST be null):
                {
                  "grammarScore": 0-10,
                  "pronunciationScore": null,
                  "vocabularyScore": null,
                  "fluencyScore": null,
                  "overallComment": "string",
                  "improvedVersion": "string",
                  "naturalSuggestion": "string",
                  "errorCount": 0,
                  "errors": [
                    {
                      "type": "GRAMMAR|VOCABULARY|EXPRESSION",
                      "originalText": "string",
                      "suggestedText": "string",
                      "explanation": "string"
                    }
                  ]
                }
                """.formatted(goalType, focusSkill, coachingMode, feedbackRules, scenarioLine, userMessage);
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

    private String buildGoalModeRoleplayRules(String goalType, String focusSkill, String coachingMode) {
        String goal = goalType.toUpperCase();
        String mode = coachingMode.toUpperCase();
        String focus = focusSkill.toUpperCase();

        String goalRules = switch (goal) {
            case "GRAMMAR" -> """
                    - GRAMMAR goal: prioritize grammatical accuracy. Correct tense, agreement, and sentence structure explicitly.
                    - Ask follow-up questions that encourage the learner to reuse corrected structures.
                    """;
            case "FLUENCY" -> """
                    - FLUENCY goal: prioritize smooth, continuous expression and natural phrasing.
                    - Avoid over-correcting minor errors in the middle of conversation flow.
                    """;
            case "COMMUNICATION" -> """
                    - COMMUNICATION goal: prioritize successful meaning delivery and confidence.
                    - Keep learner engaged in task completion, with gentle corrections only when needed.
                    """;
            default -> """
                    - General goal: keep a balanced approach between clarity, correctness, and confidence.
                    """;
        };

        String modeRules = switch (mode) {
            case "FLUENCY" -> """
                    - FLUENCY mode: keep replies short and momentum-oriented; ask one forward-driving question.
                    - Prefer reformulations that the learner can say immediately.
                    """;
            case "COACH" -> """
                    - COACH mode: balance encouragement with actionable correction and next-step guidance.
                    - Give one concrete coaching suggestion per turn when possible.
                    """;
            default -> "";
        };

        String focusRules = switch (focus) {
            case "GRAMMAR" -> "- FocusSkill GRAMMAR: emphasize structure quality in your guidance.\n";
            case "FLUENCY" -> "- FocusSkill FLUENCY: emphasize natural pacing and linking ideas.\n";
            case "VOCABULARY" -> "- FocusSkill VOCABULARY: emphasize word choice precision and collocations.\n";
            case "PRONUNCIATION" -> "- FocusSkill PRONUNCIATION: when channel is voice, give practical articulation hints.\n";
            default -> "";
        };

        return "Goal/Mode policy:\n" + goalRules + modeRules + focusRules;
    }

    private String buildGoalModeFeedbackRules(String goalType, String focusSkill, String coachingMode) {
        String goal = goalType.toUpperCase();
        String mode = coachingMode.toUpperCase();
        String focus = focusSkill.toUpperCase();

        String goalRules = switch (goal) {
            case "GRAMMAR" -> """
                    - If unsure between issues, prioritize grammar mistakes first.
                    - Ensure at least one grammar-focused recommendation when an error exists.
                    """;
            case "FLUENCY" -> """
                    - Prioritize natural flow and concise reformulation over exhaustive micro-corrections.
                    - naturalSuggestion should optimize speakability and rhythm.
                    """;
            case "COMMUNICATION" -> """
                    - Prioritize intelligibility and task success; avoid overwhelming detail.
                    - overallComment should reinforce successful communication intent.
                    """;
            default -> "";
        };

        String modeRules = switch (mode) {
            case "FLUENCY" -> """
                    - Keep corrections minimal; prefer one high-impact fix that improves flow.
                    """;
            case "COACH" -> """
                    - Include one practical coaching takeaway in overallComment or naturalSuggestion.
                    """;
            default -> "";
        };

        String focusRules = switch (focus) {
            case "GRAMMAR" -> "- FocusSkill GRAMMAR: explanations should emphasize grammar rules briefly.\n";
            case "FLUENCY" -> "- FocusSkill FLUENCY: suggestions should improve pacing and linking.\n";
            case "VOCABULARY" -> "- FocusSkill VOCABULARY: prioritize lexical precision and natural collocations.\n";
            default -> "";
        };

        return goalRules + modeRules + focusRules;
    }
}
