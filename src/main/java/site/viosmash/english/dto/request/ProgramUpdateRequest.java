package site.viosmash.english.dto.request;

import lombok.Data;

@Data
public class ProgramUpdateRequest {

    private String title;

    private Integer order;

    private Integer status;
}
