package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LearningMemoryResponse {
    private List<String> recurringErrors;
    private List<String> weakSkills;
    private List<String> masteredPatterns;
    private List<String> preferredTopics;
    private String confidenceLevel;
    private List<String> last3SessionGoals;
}
