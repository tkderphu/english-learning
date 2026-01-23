package site.viosmash.english.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Nguyen Quang Phu
 * @since 25/09/2025
 */
@Data
@NoArgsConstructor
public class UserSessionResponse {

    private int id;

    private String fullName;

    private String email;

    private String avatar;

    private LocalDateTime createdAt;

    private String remoteIp;

    private String userAgent;

    public UserSessionResponse(int id, String fullName, String email, String avatar, LocalDateTime createdAt, String remoteIp, String userAgent) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.remoteIp = remoteIp;
        this.avatar = avatar;
        this.userAgent = userAgent;
    }
}
