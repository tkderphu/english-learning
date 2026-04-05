package site.viosmash.english.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class LogLearningActivityRequest {

    @NotBlank
    @Size(max = 32)
    private String activityType;

    @Size(max = 512)
    private String title;

    @NotNull
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Integer durationSeconds;

    private BigDecimal scorePercent;

    private Integer wordsNewCount;

    private Map<String, Object> detail;

    @Size(max = 64)
    private String referenceType;

    private Integer referenceId;
}
