package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EndSessionResponse {
    private Integer sessionId;
    private String status;
    private String fluencyLevel;
    private String grammarLevel;
    private String vocabularyLevel;
    private Integer sentenceCount;
    private Integer errorCount;
    private Integer durationSeconds;
    private String nextSuggestion;
    private List<String> nextSuggestions;
}