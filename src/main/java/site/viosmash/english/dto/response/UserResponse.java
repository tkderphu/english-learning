package site.viosmash.english.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private int id;

    private String email;

    private String fullName;

    private String avatar;

    private Integer role;

    private int status;

    public UserResponse(int id, String email, String fullName, String avatar, Integer role, int status) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.avatar = avatar;
        this.status = status;
        this.role = role;
    }
}