package site.viosmash.english.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LevelResponse {
    private int id;
    private String name;
    private String description;
    private int numberCourse;
    private int status;
}
