package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LearningActivityItemResponse {
    private Integer id;
    private String activityType;
    private String title;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer durationSeconds;
    private BigDecimal scorePercent;
    private Integer wordsNewCount;
    private String detailJson;
    private String referenceType;
    private Integer referenceId;
}
