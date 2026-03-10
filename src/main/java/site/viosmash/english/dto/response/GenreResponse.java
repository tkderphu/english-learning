package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenreResponse {
    private int id;
    private String name;
    private String thumbnail;
    private String description;
    private int status;
}
