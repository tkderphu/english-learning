package site.viosmash.english.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_chat_sessions")
@Getter
@Setter
public class AiChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "scenario_id")
    private Integer scenarioId;

    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "level_id")
    private Integer levelId;

    private String title;

    @Column(name = "session_type")
    private String sessionType;

    private String status;

    @Column(name = "ai_role_snapshot")
    private String aiRoleSnapshot;

    @Column(name = "instruction_snapshot", columnDefinition = "TEXT")
    private String instructionSnapshot;

    @Column(name = "max_turns")
    private Integer maxTurns;

    @Column(name = "current_turn")
    private Integer currentTurn;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "system_prompt_snapshot", columnDefinition = "TEXT")
    private String systemPromptSnapshot;

    @Column(name = "goal_type")
    private String goalType;

    @Column(name = "focus_skill")
    private String focusSkill;

    @Column(name = "coaching_mode")
    private String coachingMode;

    @Column(name = "fluency_mode")
    private Boolean fluencyMode;

    @Column(name = "target_duration_minutes")
    private Integer targetDurationMinutes;

    @Column(name = "mission_title")
    private String missionTitle;

    @Column(name = "mission_objective", columnDefinition = "TEXT")
    private String missionObjective;

    @Column(name = "mission_success_criteria_json", columnDefinition = "TEXT")
    private String missionSuccessCriteriaJson;

    @Column(name = "mission_status")
    private String missionStatus;
}
