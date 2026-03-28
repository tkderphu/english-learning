package site.viosmash.english.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ai_scenarios")
@Getter
@Setter
public class AiScenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;

    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "level_id")
    private Integer levelId;

    private String type;

    @Column(name = "ai_role")
    private String aiRole;

    private String instruction;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "generated_by_ai")
    private Boolean generatedByAi;

    @Column(name = "generation_prompt")
    private String generationPrompt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "system_prompt", columnDefinition = "TEXT")
    private String systemPrompt;
}
