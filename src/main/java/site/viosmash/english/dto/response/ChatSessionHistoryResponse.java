package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatSessionHistoryResponse {
    private Integer sessionId;
    private String title;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer currentTurn;

    private Integer topicId;
    private Integer levelId;
    private Integer durationSeconds;
    private String nextSuggestion;
}