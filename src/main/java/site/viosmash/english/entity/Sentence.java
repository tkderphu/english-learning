package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "sentence")
@Entity
@Accessors(chain = true)
public class Sentence extends BaseEntity {

    @Column(name = "page_id")
    private Integer pageId;

    private String content;

    private String transcription1;

    @Column(name = "start_time")
    private long startTime;

    @Column(name = "end_time")
    private long endTime;
}
