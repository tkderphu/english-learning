package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "page")
@Entity
@Accessors(chain = true)
public class Page extends BaseEntity{

    @Column(name = "chapter_id")
    private Integer chapterId;

    private int number;

    @Column(name = "audio_id")
    private Integer audioId;

    @Column(columnDefinition = "TEXT")
    private String content;
}
