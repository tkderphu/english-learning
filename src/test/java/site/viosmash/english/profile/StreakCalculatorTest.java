package site.viosmash.english.profile;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreakCalculatorTest {

    @Test
    void streakCountsConsecutiveDaysEndingToday() {
        LocalDate today = LocalDate.of(2026, 4, 4);
        Set<LocalDate> days = Set.of(
                today,
                today.minusDays(1),
                today.minusDays(2)
        );
        assertEquals(3, StreakCalculator.currentStreak(days, today));
    }

    @Test
    void streakZeroWhenYesterdayMissing() {
        LocalDate today = LocalDate.of(2026, 4, 4);
        Set<LocalDate> days = Set.of(today.minusDays(2));
        assertEquals(0, StreakCalculator.currentStreak(days, today));
    }
}
