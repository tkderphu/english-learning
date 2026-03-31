package site.viosmash.english.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Schema(defaultValue = "admin@gmail.com")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Schema(defaultValue = "admin2004")
    @NotBlank(message = "Password is required")
    private String password;
}
