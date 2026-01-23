package site.viosmash.english.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Status {

    ACTIVE(1),
    INACTIVE(0);

    @Getter
    private final int value;
}

