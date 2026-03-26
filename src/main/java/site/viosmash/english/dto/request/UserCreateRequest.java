package site.viosmash.english.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateRequest {

    @NotBlank(message = "Email is required")
    @Schema(defaultValue = "admin@gmail.com")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(defaultValue = "admin2004")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(defaultValue = "admin 20024")
    @NotBlank(message = "Full name is required")
    private String fullName;
}
