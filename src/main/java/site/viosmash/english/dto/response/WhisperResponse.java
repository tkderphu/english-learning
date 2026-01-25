package site.viosmash.english.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class WhisperResponse {

    private String status;

    private List<WhisperSegment> segments;

    private String language;

    @Data
    public static class WhisperSegment {
        private String sentence;
        private Double startTime;
        private Double endTime;
    }
}