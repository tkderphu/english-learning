package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponse {
    private int id;
    private String title;
    private String language;
    private String coverUrl;
    private String authors;
    private int status;


}
