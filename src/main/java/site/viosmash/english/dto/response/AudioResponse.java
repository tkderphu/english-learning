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
    private int duration;
    private String format;
    private double sampleRate;
    private double fileSize;
    private String fileUrl;
    private int pagesId;
}
