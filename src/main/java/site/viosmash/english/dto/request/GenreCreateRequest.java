package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class GenreCreateRequest {
    private String name;
    private String thumbnail;
    private String description;
}
