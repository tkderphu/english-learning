package site.viosmash.english.dto.response;

import lombok.Data;

@Data
public class SubtitleResponse {

    private Long id;

    private String sentence;

    private Integer sentenceIndex;

    private Double startTime;

    private Double endTime;

    private String vietnameseTranslation;
}