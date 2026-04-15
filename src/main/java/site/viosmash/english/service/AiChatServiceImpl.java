package site.viosmash.english.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.request.CreateChatSessionRequest;
import site.viosmash.english.dto.request.SendTextMessageRequest;
import site.viosmash.english.dto.response.ChatMessageItemResponse;
import site.viosmash.english.dto.response.ChatMessageDetailItemResponse;
import site.viosmash.english.dto.response.ChatSessionHistoryResponse;
import site.viosmash.english.dto.response.CreateChatSessionResponse;
import site.viosmash.english.dto.response.DeleteChatSessionResponse;
import site.viosmash.english.dto.response.AiChatSessionDetailResponse;
import site.viosmash.english.dto.response.EndSessionResponse;
import site.viosmash.english.dto.response.ErrorItemResponse;
import site.viosmash.english.dto.response.FeedbackResponse;
import site.viosmash.english.dto.response.FeedbackLayersResponse;
import site.viosmash.english.dto.response.FluencySignalsResponse;
import site.viosmash.english.dto.response.SessionMissionResponse;
import site.viosmash.english.dto.response.SendTextMessageResponse;
import site.viosmash.english.entity.AiChatMessage;
import site.viosmash.english.entity.AiChatSession;
import site.viosmash.english.entity.AiMessageError;
import site.viosmash.english.entity.AiMessageFeedback;
import site.viosmash.english.entity.AiScenario;
import site.viosmash.english.entity.AiSessionSummary;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.groqapi.GroqAiClient;
import site.viosmash.english.repository.AiChatMessageRepository;
import site.viosmash.english.repository.AiChatSessionRepository;
import site.viosmash.english.repository.AiMessageErrorRepository;
import site.viosmash.english.repository.AiMessageFeedbackRepository;
import site.viosmash.english.repository.AiScenarioRepository;
import site.viosmash.english.repository.AiSessionSummaryRepository;
import site.viosmash.english.util.Util;
import site.viosmash.english.util.enums.RoleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_ENDED = "ENDED";
    private static final String SENDER_USER = "USER";
    private static final String SENDER_AI = "AI";
    private static final String INPUT_TEXT = "TEXT";
    private static final String INPUT_VOICE = "VOICE";
    private static final int DEFAULT_MAX_TURNS = 20;
    private static final int DEFAULT_RECENT_MESSAGE_LIMIT = 8;

    private final AiChatSessionRepository aiChatSessionRepository;
    private final AiChatMessageRepository aiChatMessageRepository;
    private final AiScenarioRepository aiScenarioRepository;
    private final AiRoleplayService aiRoleplayService;
    private final GroqAiClient groqAiClient;
    private final FileStorageService fileStorageService;
    private final AiMessageFeedbackRepository aiMessageFeedbackRepository;
    private final AiMessageErrorRepository aiMessageErrorRepository;
    private final AiSessionSummaryRepository aiSessionSummaryRepository;
    private final ObjectMapper objectMapper;
    private final Util util;
    private final ProfileLearningActivityService profileLearningActivityService;
    private final PersonalizedVocabularyService personalizedVocabularyService;
    private final LearningMemoryService learningMemoryService;

    @Value("${openai.model.transcription}")
    private String transcriptionModel;

    @Override
    @Transactional
    public CreateChatSessionResponse createSession(CreateChatSessionRequest request) {
        Integer currentUserId = util.getCurrentUser().getId();
        boolean isAdmin = isAdmin();

        AiScenario scenario = null;
        Integer scenarioId = request.getScenarioId();
        if (scenarioId != null && scenarioId == 0) {
            scenarioId = null; // free chat option from /scenarios endpoint
        }
        if (scenarioId != null) {
            scenario = aiScenarioRepository.findById(scenarioId)
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Scenario not found"));
        }

        AiChatSession session = new AiChatSession();
        if (request.getUserId() != null && !request.getUserId().equals(currentUserId) && !isAdmin) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Access is denied");
        }
        session.setUserId(currentUserId);
        session.setScenarioId(scenarioId);
        session.setTopicId(scenario != null ? scenario.getTopicId() : null);
        session.setLevelId(scenario != null ? scenario.getLevelId() : null);
        session.setTitle(scenario != null ? scenario.getTitle() : "Free conversation");
        session.setSessionType(scenarioId != null ? "SCENARIO" : "FREE_CHAT");
        session.setStatus(STATUS_ACTIVE);
        session.setAiRoleSnapshot(scenario != null ? scenario.getAiRole() : "English partner");
        session.setInstructionSnapshot(
                scenario != null ? scenario.getInstruction() : "Have a natural English conversation.");
        session.setSystemPromptSnapshot(scenario != null ? scenario.getSystemPrompt() : null);
        session.setMaxTurns(request.getMaxTurns() != null ? request.getMaxTurns() : DEFAULT_MAX_TURNS);
        session.setCurrentTurn(0);
        session.setGoalType(request.getGoalType());
        session.setFocusSkill(request.getFocusSkill());
        session.setCoachingMode(request.getCoachingMode());
        session.setFluencyMode(Boolean.TRUE.equals(request.getFluencyMode()));
        session.setTargetDurationMinutes(request.getTargetDurationMinutes());
        if (request.getMission() != null) {
            session.setMissionTitle(request.getMission().getTitle());
            session.setMissionObjective(request.getMission().getObjective());
            if (request.getMission().getSuccessCriteria() != null) {
                session.setMissionSuccessCriteriaJson(request.getMission().getSuccessCriteria().stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .collect(Collectors.joining("|")));
            }
            session.setMissionStatus("ACTIVE");
        }
        session.setStartedAt(LocalDateTime.now());
        session.setLastMessageAt(LocalDateTime.now());
        String memoryBlock = learningMemoryService.toPromptBlock(
                currentUserId,
                request.getGoalType(),
                request.getFocusSkill(),
                request.getCoachingMode()
        );
        String baseSystemPrompt = session.getSystemPromptSnapshot();
        if (baseSystemPrompt == null || baseSystemPrompt.isBlank()) {
            session.setSystemPromptSnapshot(memoryBlock);
        } else {
            session.setSystemPromptSnapshot(baseSystemPrompt + "\n\n" + memoryBlock);
        }

        AiChatSession savedSession = aiChatSessionRepository.save(session);

        appendOpeningAiMessage(savedSession);

        return CreateChatSessionResponse.builder()
                .sessionId(savedSession.getId())
                .title(savedSession.getTitle())
                .aiRole(savedSession.getAiRoleSnapshot())
                .instruction(savedSession.getInstructionSnapshot())
                .status(savedSession.getStatus())
                .currentTurn(savedSession.getCurrentTurn())
                .goalType(savedSession.getGoalType())
                .focusSkill(savedSession.getFocusSkill())
                .coachingMode(savedSession.getCoachingMode())
                .fluencyMode(savedSession.getFluencyMode())
                .targetDurationMinutes(savedSession.getTargetDurationMinutes())
                .mission(toMissionResponse(savedSession))
                .build();
    }

    /**
     * AI speaks first (turn 0). {@link #processUserMessage} still uses {@code currentTurn + 1} for the first learner turn.
     */
    private void appendOpeningAiMessage(AiChatSession session) {
        if (session == null || session.getId() == null) {
            return;
        }
        String opening = null;
        try {
            opening = aiRoleplayService.generateOpeningLine(session);
        } catch (Exception ex) {
            log.warn("AI opening line failed for session {}", session.getId(), ex);
        }
        if (opening == null || opening.isBlank()) {
            opening = buildFallbackOpeningLine(session);
        }
        AiChatMessage open = new AiChatMessage();
        open.setSessionId(session.getId());
        open.setSenderType(SENDER_AI);
        open.setInputType(INPUT_TEXT);
        open.setTurnNumber(0);
        open.setContent(opening.trim());
        aiChatMessageRepository.save(open);
        session.setLastMessageAt(LocalDateTime.now());
        aiChatSessionRepository.save(session);
    }

    private String buildFallbackOpeningLine(AiChatSession session) {
        boolean scenario = session.getScenarioId() != null
                || (session.getSessionType() != null && session.getSessionType().equalsIgnoreCase("SCENARIO"));
        String instruction = session.getInstructionSnapshot() == null ? "" : session.getInstructionSnapshot().trim();
        if (scenario) {
            if (!instruction.isBlank()) {
                return "Hi! Let's begin. " + instruction;
            }
            return "Hi! Welcome to this scenario. Let's start whenever you're ready.";
        }
        return "Hi! Nice to meet you. What would you like to talk about today?";
    }

    @Override
    @Transactional
    public SendTextMessageResponse sendTextMessage(Integer sessionId, SendTextMessageRequest request) {
        if (sessionId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "sessionId is required");
        }
        String content = request.getContent();
        if (content == null || content.isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "content is required");
        }

        return processUserMessage(sessionId, content.trim(), INPUT_TEXT, null, null, null);
    }

    @Override
    @Transactional
    public SendTextMessageResponse sendVoiceMessage(Integer sessionId, MultipartFile audioFile, Integer audioDuration) {
        if (sessionId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "sessionId is required");
        }
        if (audioFile == null || audioFile.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "audio file is required");
        }

        String transcript;
        try {
            transcript = groqAiClient.transcribeAudio(audioFile, transcriptionModel);
        } catch (Exception ex) {
            throw new ServiceException(HttpStatus.BAD_GATEWAY, "Voice transcription failed");
        }

        String audioUrl = fileStorageService.store(audioFile).getUrl();
        return processUserMessage(sessionId, transcript, INPUT_VOICE, transcript, audioUrl, audioDuration);
    }

    private SendTextMessageResponse processUserMessage(
            Integer sessionId,
            String userContent,
            String inputType,
            String sttTranscript,
            String audioUrl,
            Integer audioDuration) {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        AiChatSession session = aiChatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Session not found"));

        assertSessionOwner(session);

        if (!STATUS_ACTIVE.equalsIgnoreCase(session.getStatus())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Session is not active");
        }

        Integer nextTurn = (session.getCurrentTurn() == null ? 0 : session.getCurrentTurn()) + 1;

        AiChatMessage userMessage = new AiChatMessage();
        userMessage.setSessionId(sessionId);
        userMessage.setSenderType(SENDER_USER);
        userMessage.setInputType(inputType);
        userMessage.setTurnNumber(nextTurn);
        userMessage.setContent(userContent);
        userMessage.setSttTranscript(sttTranscript);
        userMessage.setAudioUrl(audioUrl);
        userMessage.setAudioDuration(audioDuration);
        AiChatMessage savedUserMessage = aiChatMessageRepository.save(userMessage);

        List<AiChatMessage> allMessages = aiChatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        List<AiChatMessage> recentMessages = allMessages.size() > DEFAULT_RECENT_MESSAGE_LIMIT
                ? allMessages.subList(allMessages.size() - DEFAULT_RECENT_MESSAGE_LIMIT, allMessages.size())
                : allMessages;

        List<String> personalizedWords = personalizedVocabularyService.selectWordsForTurn(
                session.getUserId(),
                session,
                recentMessages,
                userContent,
                nextTurn
        );
        log.info("AI personalization - sessionId={}, userId={}, turn={}, words={}",
                sessionId, session.getUserId(), nextTurn, personalizedWords);

        String aiReply = aiRoleplayService.generateReply(session, recentMessages, userContent, personalizedWords);
        String feedbackJson = aiRoleplayService.generateFeedbackJson(session, userContent, inputType);

        AiChatMessage aiMessage = new AiChatMessage();
        aiMessage.setSessionId(sessionId);
        aiMessage.setSenderType(SENDER_AI);
        aiMessage.setInputType(INPUT_TEXT);
        aiMessage.setTurnNumber(nextTurn);
        aiMessage.setContent(aiReply);
        AiChatMessage savedAiMessage = aiChatMessageRepository.save(aiMessage);

        session.setCurrentTurn(nextTurn);
        session.setLastMessageAt(LocalDateTime.now());
        if (session.getMaxTurns() != null && nextTurn >= session.getMaxTurns()) {
            session.setStatus(STATUS_ENDED);
            session.setEndedAt(LocalDateTime.now());
        }
        aiChatSessionRepository.save(session);

        FeedbackResponse feedbackResponse = saveAndBuildFeedback(savedUserMessage.getId(), feedbackJson, inputType);

        return SendTextMessageResponse.builder()
                .sessionId(sessionId)
                .turnNumber(nextTurn)
                .userMessage(toMessageItem(savedUserMessage))
                .aiMessage(toMessageItem(savedAiMessage))
                .feedback(feedbackResponse)
                .build();
    }

    @Override
    @Transactional
    public EndSessionResponse endSession(Integer sessionId) {
        if (sessionId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "sessionId is required");
        }
        AiChatSession session = aiChatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Session not found"));

        assertSessionOwner(session);

        boolean wasActive = STATUS_ACTIVE.equalsIgnoreCase(session.getStatus());

        if (!STATUS_ENDED.equalsIgnoreCase(session.getStatus())) {
            session.setStatus(STATUS_ENDED);
            session.setEndedAt(LocalDateTime.now());
            aiChatSessionRepository.save(session);
        }

        List<AiChatMessage> messages = aiChatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        int durationSeconds = 0;
        if (session.getStartedAt() != null && session.getEndedAt() != null) {
            durationSeconds = (int) ChronoUnit.SECONDS.between(session.getStartedAt(), session.getEndedAt());
            if (durationSeconds < 0) {
                durationSeconds = 0;
            }
        }

        String summaryJson = aiRoleplayService.generateSummaryJson(session, messages);
        EndSessionResponse summaryResponse = saveAndBuildSummary(sessionId, durationSeconds, summaryJson, messages);

        if (wasActive && session.getUserId() != null) {
            try {
                profileLearningActivityService.logAiChatSessionEnded(session.getUserId(), session, durationSeconds);
            } catch (Exception ex) {
                log.warn("Could not record learning activity for AI chat session {}", sessionId, ex);
            }
        }

        return EndSessionResponse.builder()
                .sessionId(summaryResponse.getSessionId())
                .status(session.getStatus())
                .fluencyLevel(summaryResponse.getFluencyLevel())
                .grammarLevel(summaryResponse.getGrammarLevel())
                .vocabularyLevel(summaryResponse.getVocabularyLevel())
                .sentenceCount(summaryResponse.getSentenceCount())
                .errorCount(summaryResponse.getErrorCount())
                .durationSeconds(summaryResponse.getDurationSeconds())
                .nextSuggestion(summaryResponse.getNextSuggestion())
                .nextSuggestions(summaryResponse.getNextSuggestions())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatSessionHistoryResponse> getSessionHistory() {
        Integer userId = util.getCurrentUser().getId();

        return aiChatSessionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(s -> STATUS_ENDED.equalsIgnoreCase(s.getStatus()))
                .map(session -> {
                    AiSessionSummary sum = aiSessionSummaryRepository.findBySessionId(session.getId()).orElse(null);
                    Integer durationSeconds = sum != null ? sum.getDurationSeconds() : null;
                    List<String> nextSuggestions = parseNextSuggestionsFromStored(
                            sum != null ? sum.getNextSuggestion() : null);
                    String nextSuggestionFirst = nextSuggestions.isEmpty() ? null : nextSuggestions.get(0);

                    return ChatSessionHistoryResponse.builder()
                            .sessionId(session.getId())
                            .title(session.getTitle())
                            .status(session.getStatus())
                            .startedAt(session.getStartedAt())
                            .endedAt(session.getEndedAt())
                            .currentTurn(session.getCurrentTurn())
                            .topicId(session.getTopicId())
                            .levelId(session.getLevelId())
                            .durationSeconds(durationSeconds)
                            .nextSuggestion(nextSuggestionFirst)
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageItemResponse> getSessionMessages(Integer sessionId) {
        if (sessionId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "sessionId is required");
        }
        AiChatSession session = aiChatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Session not found"));

        assertSessionOwner(session);

        return aiChatMessageRepository.findBySessionIdOrderByCreatedAtAsc(session.getId()).stream()
                .map(this::toMessageItem)
                .toList();
    }

    @Override
    @Transactional
    public DeleteChatSessionResponse deleteSession(Integer sessionId) {
        if (sessionId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "sessionId is required");
        }

        AiChatSession session = aiChatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Session not found"));

        assertSessionOwner(session);

        // Delete in correct order to avoid orphan feedback/errors.
        List<AiChatMessage> messages = aiChatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        for (AiChatMessage msg : messages) {
            Integer messageId = msg.getId();
            if (messageId == null) {
                continue;
            }
            Optional<AiMessageFeedback> feedbackOpt = aiMessageFeedbackRepository.findByMessageId(messageId);
            if (feedbackOpt.isPresent()) {
                AiMessageFeedback feedback = feedbackOpt.get();
                List<AiMessageError> errors = aiMessageErrorRepository.findByFeedbackId(feedback.getId());
                if (errors != null && !errors.isEmpty()) {
                    aiMessageErrorRepository.deleteAll(errors);
                }
                aiMessageFeedbackRepository.delete(feedback);
            }
            aiChatMessageRepository.deleteById(messageId);
        }

        aiChatSessionRepository.deleteById(sessionId);

        return DeleteChatSessionResponse.builder()
                .sessionId(sessionId)
                .status(STATUS_ENDED.equalsIgnoreCase(session.getStatus()) ? STATUS_ENDED : session.getStatus())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AiChatSessionDetailResponse getSessionDetail(Integer sessionId) {
        if (sessionId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "sessionId is required");
        }

        AiChatSession session = aiChatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Session not found"));

        assertSessionOwner(session);

        AiSessionSummary summary = aiSessionSummaryRepository.findBySessionId(sessionId).orElse(null);

        Integer durationSeconds;
        if (summary != null && summary.getDurationSeconds() != null) {
            durationSeconds = summary.getDurationSeconds();
        } else if (session.getStartedAt() != null && session.getEndedAt() != null) {
            durationSeconds = Math.max(0,
                    (int) ChronoUnit.SECONDS.between(session.getStartedAt(), session.getEndedAt()));
        } else {
            durationSeconds = 0;
        }

        List<String> nextSuggestions = parseNextSuggestionsFromStored(
                summary != null ? summary.getNextSuggestion() : null);
        String nextSuggestionFirst = nextSuggestions.isEmpty() ? null : nextSuggestions.get(0);

        return AiChatSessionDetailResponse.builder()
                .sessionId(session.getId())
                .title(session.getTitle())
                .sessionType(session.getSessionType())
                .scenarioId(session.getScenarioId())
                .topicId(session.getTopicId())
                .levelId(session.getLevelId())
                .aiRole(session.getAiRoleSnapshot())
                .instruction(session.getInstructionSnapshot())
                .status(session.getStatus())
                .currentTurn(session.getCurrentTurn())
                .maxTurns(session.getMaxTurns())
                .goalType(session.getGoalType())
                .focusSkill(session.getFocusSkill())
                .coachingMode(session.getCoachingMode())
                .fluencyMode(session.getFluencyMode())
                .targetDurationMinutes(session.getTargetDurationMinutes())
                .mission(toMissionResponse(session))
                .startedAt(session.getStartedAt())
                .endedAt(session.getEndedAt())
                .durationSeconds(durationSeconds)
                .fluencyLevel(summary != null ? summary.getFluencyLevel() : null)
                .grammarLevel(summary != null ? summary.getGrammarLevel() : null)
                .vocabularyLevel(summary != null ? summary.getVocabularyLevel() : null)
                .sentenceCount(summary != null ? summary.getSentenceCount() : null)
                .errorCount(summary != null ? summary.getErrorCount() : null)
                .nextSuggestion(nextSuggestionFirst)
                .nextSuggestions(nextSuggestions)
                .relatedTopics(summary != null ? summary.getRelatedTopics() : null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDetailItemResponse> getSessionTranscript(Integer sessionId) {
        if (sessionId == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "sessionId is required");
        }

        AiChatSession session = aiChatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Session not found"));

        assertSessionOwner(session);

        return aiChatMessageRepository.findBySessionIdOrderByCreatedAtAsc(session.getId()).stream()
                .map(m -> {
                    FeedbackResponse feedback = null;
                    if (SENDER_USER.equalsIgnoreCase(m.getSenderType())) {
                        Integer messageId = m.getId();
                        if (messageId != null) {
                            var fbOpt = aiMessageFeedbackRepository.findByMessageId(messageId);
                            if (fbOpt.isPresent()) {
                                feedback = toFeedbackWithErrors(fbOpt.get());
                            }
                        }
                    }
                    return ChatMessageDetailItemResponse.builder()
                            .id(m.getId())
                            .senderType(m.getSenderType())
                            .inputType(m.getInputType())
                            .turnNumber(m.getTurnNumber())
                            .content(m.getContent())
                            .createdAt(m.getCreatedAt())
                            .feedback(feedback)
                            .build();
                })
                .toList();
    }

    private FeedbackResponse toFeedbackWithErrors(AiMessageFeedback feedback) {
        List<AiMessageError> errors = aiMessageErrorRepository.findByFeedbackId(feedback.getId());
        List<ErrorItemResponse> mappedErrors = new ArrayList<>();
        if (errors != null) {
            for (AiMessageError e : errors) {
                mappedErrors.add(ErrorItemResponse.builder()
                        .type(e.getErrorType())
                        .originalText(e.getOriginalText())
                        .suggestedText(e.getSuggestedText())
                        .explanation(e.getExplanation())
                        .build());
            }
        }

        return FeedbackResponse.builder()
                .pronunciationScore(feedback.getPronunciationScore())
                .grammarScore(feedback.getGrammarScore())
                .vocabularyScore(feedback.getVocabularyScore())
                .fluencyScore(feedback.getFluencyScore())
                .overallComment(feedback.getOverallComment())
                .improvedVersion(feedback.getImprovedVersion())
                .naturalSuggestion(feedback.getNaturalSuggestion())
                .errors(mappedErrors)
                .feedbackLayers(buildFeedbackLayers(feedback.getOverallComment(), feedback.getNaturalSuggestion()))
                .build();
    }

    private ChatMessageItemResponse toMessageItem(AiChatMessage message) {
        return ChatMessageItemResponse.builder()
                .id(message.getId())
                .senderType(message.getSenderType())
                .inputType(message.getInputType())
                .turnNumber(message.getTurnNumber())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private FeedbackResponse saveAndBuildFeedback(Integer messageId, String feedbackJson, String inputType) {
        try {
            Map<String, Object> payload = objectMapper.readValue(feedbackJson, new TypeReference<>() {
            });
            String layer1Tip = toStringValue(payload.get("layer1Tip"));
            String layer2Explanation = toStringValue(payload.get("layer2Explanation"));
            String layer2Example = toStringValue(payload.get("layer2Example"));
            Integer pauseDensity = toInteger(payload.get("pauseDensity"));
            Integer fillerWords = toInteger(payload.get("fillerWords"));
            Integer continuousLength = toInteger(payload.get("continuousLength"));
            Integer flowProgress = toInteger(payload.get("flowProgress"));
            boolean typedOnly = INPUT_TEXT.equalsIgnoreCase(inputType);
            BigDecimal grammarScore = toDecimal(payload.get("grammarScore"));
            BigDecimal vocabularyScore = typedOnly ? null : toDecimal(payload.get("vocabularyScore"));
            BigDecimal fluencyScore = typedOnly ? null : toDecimal(payload.get("fluencyScore"));
            BigDecimal pronunciationScore = typedOnly ? null : toDecimal(payload.get("pronunciationScore"));
            String overallComment = toStringValue(payload.get("overallComment"));
            String improvedVersion = toStringValue(payload.get("improvedVersion"));
            String naturalSuggestion = toStringValue(payload.get("naturalSuggestion"));
            Integer errorCount = toInteger(payload.get("errorCount"));

            AiMessageFeedback feedback = new AiMessageFeedback();
            feedback.setMessageId(messageId);
            feedback.setGrammarScore(grammarScore);
            feedback.setVocabularyScore(vocabularyScore);
            feedback.setFluencyScore(fluencyScore);
            feedback.setPronunciationScore(pronunciationScore);
            feedback.setOverallComment(overallComment);
            feedback.setImprovedVersion(improvedVersion);
            feedback.setNaturalSuggestion(naturalSuggestion);
            feedback.setErrorCount(errorCount != null ? errorCount : 0);
            AiMessageFeedback savedFeedback = aiMessageFeedbackRepository.save(feedback);

            List<ErrorItemResponse> errorItems = new ArrayList<>();
            Object rawErrors = payload.get("errors");
            if (rawErrors instanceof List<?> rawList) {
                for (Object item : rawList) {
                    if (!(item instanceof Map<?, ?> rawMap)) {
                        continue;
                    }
                    String type = toStringValue(rawMap.get("type"));
                    if (typedOnly && type != null && type.equalsIgnoreCase("PRONUNCIATION")) {
                        continue;
                    }
                    String originalText = toStringValue(rawMap.get("originalText"));
                    String suggestedText = toStringValue(rawMap.get("suggestedText"));
                    String explanation = toStringValue(rawMap.get("explanation"));

                    AiMessageError error = new AiMessageError();
                    error.setFeedbackId(savedFeedback.getId());
                    error.setErrorType(type);
                    error.setOriginalText(originalText);
                    error.setSuggestedText(suggestedText);
                    error.setExplanation(explanation);
                    aiMessageErrorRepository.save(error);

                    errorItems.add(ErrorItemResponse.builder()
                            .type(type)
                            .originalText(originalText)
                            .suggestedText(suggestedText)
                            .explanation(explanation)
                            .build());
                }
            }

            return FeedbackResponse.builder()
                    .pronunciationScore(pronunciationScore)
                    .grammarScore(grammarScore)
                    .vocabularyScore(vocabularyScore)
                    .fluencyScore(fluencyScore)
                    .overallComment(overallComment)
                    .improvedVersion(improvedVersion)
                    .naturalSuggestion(naturalSuggestion)
                    .errors(errorItems)
                    .feedbackLayers(
                            FeedbackLayersResponse.builder()
                                    .layer1Tip(defaultIfBlank(layer1Tip, overallComment))
                                    .layer2Explanation(defaultIfBlank(layer2Explanation, naturalSuggestion))
                                    .layer2Example(layer2Example)
                                    .build()
                    )
                    .fluencySignals(
                            FluencySignalsResponse.builder()
                                    .pauseDensity(pauseDensity)
                                    .fillerWords(fillerWords)
                                    .continuousLength(continuousLength)
                                    .flowProgress(flowProgress)
                                    .build()
                    )
                    .build();
        } catch (Exception ex) {
            throw new ServiceException(HttpStatus.BAD_GATEWAY, "AI feedback format is invalid");
        }
    }

    private EndSessionResponse saveAndBuildSummary(
            Integer sessionId,
            Integer durationSeconds,
            String summaryJson,
            List<AiChatMessage> messages) {
        try {
            Map<String, Object> payload = objectMapper.readValue(summaryJson, new TypeReference<>() {
            });

            Integer fallbackSentenceCount = (int) messages.stream()
                    .filter(m -> SENDER_USER.equalsIgnoreCase(m.getSenderType()))
                    .count();

            String fluencyLevel = defaultIfBlank(toStringValue(payload.get("fluencyLevel")), "FAIR");
            String grammarLevel = defaultIfBlank(toStringValue(payload.get("grammarLevel")), "FAIR");
            String vocabularyLevel = defaultIfBlank(toStringValue(payload.get("vocabularyLevel")), "FAIR");
            Integer sentenceCount = toInteger(payload.get("sentenceCount"));
            Integer errorCount = toInteger(payload.get("errorCount"));
            List<String> nextSuggestions = parseNextSuggestions(payload.get("nextSuggestions"),
                    payload.get("nextSuggestion"));
            String nextSuggestionFirst = nextSuggestions.isEmpty()
                    ? "Continue this topic and try more varied sentence patterns."
                    : nextSuggestions.get(0);
            String relatedTopics = toStringValue(payload.get("relatedTopics"));

            AiSessionSummary summary = aiSessionSummaryRepository.findBySessionId(sessionId)
                    .orElseGet(AiSessionSummary::new);
            summary.setSessionId(sessionId);
            summary.setFluencyLevel(fluencyLevel);
            summary.setGrammarLevel(grammarLevel);
            summary.setVocabularyLevel(vocabularyLevel);
            summary.setSentenceCount(sentenceCount != null ? sentenceCount : fallbackSentenceCount);
            summary.setErrorCount(errorCount != null ? errorCount : 0);
            summary.setDurationSeconds(durationSeconds);
            // Store as JSON array string in the existing column.
            summary.setNextSuggestion(objectMapper.writeValueAsString(nextSuggestions));
            summary.setRelatedTopics(relatedTopics);
            AiSessionSummary savedSummary = aiSessionSummaryRepository.save(summary);

            return EndSessionResponse.builder()
                    .sessionId(savedSummary.getSessionId())
                    .status(STATUS_ENDED)
                    .fluencyLevel(savedSummary.getFluencyLevel())
                    .grammarLevel(savedSummary.getGrammarLevel())
                    .vocabularyLevel(savedSummary.getVocabularyLevel())
                    .sentenceCount(savedSummary.getSentenceCount())
                    .errorCount(savedSummary.getErrorCount())
                    .durationSeconds(savedSummary.getDurationSeconds())
                    .nextSuggestion(nextSuggestionFirst)
                    .nextSuggestions(nextSuggestions)
                    .build();
        } catch (Exception ex) {
            throw new ServiceException(HttpStatus.BAD_GATEWAY, "AI summary format is invalid");
        }
    }

    private List<String> parseNextSuggestions(Object nextSuggestionsValue, Object nextSuggestionValue) {
        List<String> nextSuggestions = new ArrayList<>();

        if (nextSuggestionsValue instanceof List<?> rawList) {
            for (Object o : rawList) {
                String s = toStringValue(o);
                if (s != null && !s.isBlank()) {
                    nextSuggestions.add(s.trim());
                }
            }
        } else if (nextSuggestionsValue instanceof String s && !s.isBlank()) {
            // Sometimes model may return JSON array as string.
            String trimmed = s.trim();
            if (trimmed.startsWith("[")) {
                nextSuggestions = tryParseJsonStringList(trimmed);
            } else {
                nextSuggestions = splitSuggestionText(trimmed);
            }
        }

        if (nextSuggestions.isEmpty() && nextSuggestionValue != null) {
            String s = toStringValue(nextSuggestionValue);
            if (s != null && !s.isBlank()) {
                nextSuggestions = splitSuggestionText(s);
            }
        }

        return nextSuggestions;
    }

    private List<String> parseNextSuggestionsFromStored(String storedValue) {
        if (storedValue == null || storedValue.isBlank()) {
            return Collections.emptyList();
        }
        String trimmed = storedValue.trim();
        if (trimmed.startsWith("[")) {
            return tryParseJsonStringList(trimmed);
        }
        return splitSuggestionText(trimmed);
    }

    private List<String> tryParseJsonStringList(String jsonArray) {
        try {
            return objectMapper.readValue(jsonArray, new TypeReference<List<String>>() {
            });
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    private List<String> splitSuggestionText(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        // Accept common separators: | ; , newline
        String[] parts = text.split("\\s*[\\|;,\n]\\s*");
        List<String> res = new ArrayList<>();
        for (String p : parts) {
            if (p != null && !p.isBlank()) {
                res.add(p.trim());
            }
        }
        return res;
    }

    private BigDecimal toDecimal(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return new BigDecimal(String.valueOf(value));
        } catch (Exception ex) {
            return null;
        }
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception ex) {
            return null;
        }
    }

    private String toStringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private FeedbackLayersResponse buildFeedbackLayers(String overallComment, String naturalSuggestion) {
        return FeedbackLayersResponse.builder()
                .layer1Tip(overallComment)
                .layer2Explanation(naturalSuggestion)
                .layer2Example(null)
                .layer3Checkpoint(null)
                .layer3NextAction(null)
                .build();
    }

    private SessionMissionResponse toMissionResponse(AiChatSession session) {
        String raw = session.getMissionSuccessCriteriaJson();
        List<String> criteria = raw == null || raw.isBlank()
                ? List.of()
                : List.of(raw.split("\\|")).stream().map(String::trim).filter(s -> !s.isBlank()).toList();
        if ((session.getMissionTitle() == null || session.getMissionTitle().isBlank())
                && (session.getMissionObjective() == null || session.getMissionObjective().isBlank())
                && criteria.isEmpty()) {
            return null;
        }
        return SessionMissionResponse.builder()
                .title(session.getMissionTitle())
                .objective(session.getMissionObjective())
                .successCriteria(criteria)
                .status(session.getMissionStatus())
                .build();
    }

    private boolean isAdmin() {
        Integer role = util.getCurrentUser().getRole();
        return role != null && role == RoleType.ADMIN.getValue();
    }

    private void assertSessionOwner(AiChatSession session) {
        if (session == null) {
            return;
        }
        if (isAdmin()) {
            return;
        }

        Integer currentUserId = util.getCurrentUser().getId();
        if (session.getUserId() == null || !session.getUserId().equals(currentUserId)) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Access is denied");
        }
    }
}
