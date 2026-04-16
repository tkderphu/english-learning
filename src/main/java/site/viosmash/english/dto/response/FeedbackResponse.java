package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class FeedbackResponse {
    private BigDecimal pronunciationScore;
    private BigDecimal grammarScore;
    private BigDecimal vocabularyScore;
    private BigDecimal fluencyScore;
    private String overallComment;
    private String improvedVersion;
    private String naturalSuggestion;
    private List<ErrorItemResponse> errors;
    private FeedbackLayersResponse feedbackLayers;
    private FluencySignalsResponse fluencySignals;
}