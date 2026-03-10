package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorResponse {
    private int id;
    private String name;
    private String avatar;
    private String nationality;
    private String biography;
    private int status;
}
