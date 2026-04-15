package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AiChatSessionDetailResponse {
    private Integer sessionId;
    private String title;
    private String sessionType;

    private Integer scenarioId;
    private Integer topicId;
    private Integer levelId;

    private String aiRole;
    private String instruction;

    private String status;
    private Integer currentTurn;
    private Integer maxTurns;
    private String goalType;
    private String focusSkill;
    private String coachingMode;
    private Boolean fluencyMode;
    private Integer targetDurationMinutes;
    private SessionMissionResponse mission;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer durationSeconds;

    // Summary (only when ended)
    private String fluencyLevel;
    private String grammarLevel;
    private String vocabularyLevel;
    private Integer sentenceCount;
    private Integer errorCount;
    private String nextSuggestion;
    private String relatedTopics;

    private java.util.List<String> nextSuggestions;
}

