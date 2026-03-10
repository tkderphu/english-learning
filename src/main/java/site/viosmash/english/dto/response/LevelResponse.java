package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LevelResponse {
    private int id;
    private String name;
    private String description;
    private int numberCourse;
    private int status;
}
