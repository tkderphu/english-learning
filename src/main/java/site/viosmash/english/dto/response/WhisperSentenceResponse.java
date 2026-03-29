package site.viosmash.english.dto.response;

import lombok.Data;

@Data
public class WhisperSentenceResponse {

    private String englishText;

    private String vietnameseText;

    private Integer startTimeMs;

    private Integer endTimeMs;

    private Integer order;
}
