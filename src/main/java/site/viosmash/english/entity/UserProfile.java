package site.viosmash.english.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "user_profile")
@Entity
@Accessors(chain = true)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "learning_goal")
    private String learningGoal;

    @Column(name = "daily_goal_minutes")
    private int dailyGoalMinutes;

    @Column(name = "level_id")
    private Integer levelId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "after_login_onboarding_completed", nullable = false)
    private boolean afterLoginOnboardingCompleted;
}
