package site.viosmash.english.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Nguyen Quang Phu
 * @since 19/09/2025
 */
@AllArgsConstructor
public enum RoleType {
    ADMIN(1),
    USER(2);

    @Getter
    private final int value;
}
