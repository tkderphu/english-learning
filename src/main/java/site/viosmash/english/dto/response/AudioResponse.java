package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudioResponse {
    private Integer id;
    private double duration;
    private String format;
    private double sampleRate;
    private double fileSize;
    private String fileUrl;
}
