package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookPageResponse {
    private int id;
    private int number;
    private AudioResponse audio;
    private List<SentenceResponse> sentence;


    public BookPageResponse(int id, int number, AudioResponse audio) {
        this.id = id;
        this.number = number;
        this.audio = audio;
    }
}
