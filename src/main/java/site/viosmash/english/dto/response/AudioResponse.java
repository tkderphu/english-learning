package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudioResponse {
    private int id;
    private long duration;
    private String format;
    private double sampleRate;
    private double fileSize;
    private String fileUrl;
    private int pagesId;

    public AudioResponse(int id, long duration, String format, double sampleRate, double fileSize, String fileUrl) {
        this.id = id;
        this.duration = duration;
        this.format = format;
        this.sampleRate = sampleRate;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
    }
}
