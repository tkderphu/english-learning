package site.viosmash.english.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileMeResponse {
    private Integer userId;
    private String email;
    /** Tên hiển thị (full name). */
    private String fullName;
    private String avatarUrl;
    private String location;
    /** Cấp độ dạng text (trên bảng user), ví dụ BEGINNER / INTERMEDIATE. */
    private String learningLevel;
    /** Từ bảng user_profile + level (nếu có). */
    private Integer levelId;
    private String levelName;
    private Integer dailyGoalMinutes;
    private String learningGoal;
    private String jobTitle;
}
