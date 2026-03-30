package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private int sampleRate;
    private int fileSize;
    private String fileUrl;
    private int pagesId;
}
