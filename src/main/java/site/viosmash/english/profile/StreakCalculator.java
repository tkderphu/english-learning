package site.viosmash.english.profile;

import java.time.LocalDate;
import java.util.Set;

/**
 * Tính streak: ngày liên tiếp có hoạt động, kết thúc tại hôm nay (nếu có học) hoặc hôm qua.
 */
public final class StreakCalculator {

    private StreakCalculator() {
    }

    /**
     * @param daysWithActivity tập ngày (UTC/ngày local server) đã có ít nhất một hoạt động
     * @param today            ngày “hôm nay” để neo streak
     */
    public static int currentStreak(Set<LocalDate> daysWithActivity, LocalDate today) {
        if (daysWithActivity == null || daysWithActivity.isEmpty()) {
            return 0;
        }
        LocalDate anchor = daysWithActivity.contains(today) ? today : today.minusDays(1);
        if (!daysWithActivity.contains(anchor)) {
            return 0;
        }
        int streak = 0;
        LocalDate cursor = anchor;
        while (daysWithActivity.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }
}
