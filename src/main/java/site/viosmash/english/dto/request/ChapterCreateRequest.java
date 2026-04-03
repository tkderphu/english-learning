package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class ChapterCreateRequest {

    private Integer bookId;

    private String title;

    private String description;
}
