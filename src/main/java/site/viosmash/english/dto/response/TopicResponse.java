package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicResponse {
    private int id;
    private String name;
    private String description;
    private String thumbnail;
    private int status;
}
