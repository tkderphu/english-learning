package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class LevelCreateRequest {
    private String name;
    private String description;
    private int numberCourse;
}
