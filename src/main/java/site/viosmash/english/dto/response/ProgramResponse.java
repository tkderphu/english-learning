package site.viosmash.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgramResponse {

    private int id;

    private String title;

    private int order;

    private int status;
}
