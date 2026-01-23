package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Table(name = "user_session")
@Entity
@Accessors(chain = true)
public class UserSession extends BaseEntity {

    @Column(name = "user_id")
    private int userId;

    @Column(unique = true, name = "refresh_token")
    private String refreshToken;

    @Column(name = "remote_ip")
    private String remoteIp;

    @Column(name = "user_agent")
    private String userAgent;
}