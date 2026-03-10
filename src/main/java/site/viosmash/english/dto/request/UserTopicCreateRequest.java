package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class UserTopicCreateRequest {
    private int topicId;
    private int userId;
}
