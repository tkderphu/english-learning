package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ActivityDayDetailResponse {
    private LocalDate date;
    private int totalMinutes;
    private long activityCount;
    private BigDecimal averageScorePercent;
    /** Gợi ý/ngắn (có thể null nếu chưa có logic AI). */
    private String dailyInsight;
    private List<LearningActivityItemResponse> activities;
}
