package site.viosmash.english.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeckStudyCompleteRequest {

    @NotNull
    @Min(1)
    @Max(86400)
    private Integer durationSeconds;

    /** Số thẻ đã xem trong phiên (tùy chọn). */
    @Min(0)
    private Integer cardsReviewed;

    @Min(0)
    private Integer quizCorrect;

    @Min(0)
    private Integer quizTotal;
}
