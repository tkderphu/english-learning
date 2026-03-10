package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class AuthorCreateRequest {
    private String name;
    private String avatar;
    private String nationality;
    private String biography;
}
