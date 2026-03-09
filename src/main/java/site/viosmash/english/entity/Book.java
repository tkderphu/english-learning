package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "book")
@Entity
@Accessors(chain = true)
public class Book extends BaseEntity {

    private String title;

    private String language;

    @Column(name = "cover_url")
    private String coverUrl;
}
