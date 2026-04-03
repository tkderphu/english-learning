package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Table(name = "audio")
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Audio extends BaseEntity {

    private long duration;

    private String format;

    @Column(name = "sample_rate")
    private double sampleRate;

    @Column(name = "file_size")
    private double fileSize;

    @Column(name = "file_url")
    private String fileUrl;
}