package site.viosmash.english.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileUpdateRequest {

    @Size(max = 200)
    private String fullName;

    @Size(max = 300)
    private String location;

    @Size(max = 64)
    private String learningLevel;
}
