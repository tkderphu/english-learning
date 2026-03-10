package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class TopicCreateRequest {
    private String name;
    private String description;
    private String thumbnail;
}
