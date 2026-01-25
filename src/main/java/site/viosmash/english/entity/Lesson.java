package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "lesson")
@NoArgsConstructor
@AllArgsConstructor
public class Lesson extends BaseEntity {

    @Column(name = "program_id")
    private int programId;

    private String title;

    @Column(name = "audio_url")
    private String audioUrl;

    private int order;
}
