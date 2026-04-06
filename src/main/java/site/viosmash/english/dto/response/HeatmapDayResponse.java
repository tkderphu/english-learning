package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Một ô trên heatmap: mức độ 0–3 theo tổng thời lượng trong ngày.
 */
@Data
@Builder
public class HeatmapDayResponse {
    private LocalDate date;
    /** 0 = không học, 1 = ít, 2 = vừa, 3 = nhiều */
    private int intensity;
    private int totalMinutes;
    private long activityCount;
}
