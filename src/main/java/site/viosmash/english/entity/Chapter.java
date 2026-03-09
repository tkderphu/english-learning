package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "chapter")
@Entity
@Accessors(chain = true)
public class Chapter extends BaseEntity {

    @Column(name = "book_id")
    private int bookId;

    private String title;

    private String description;

    private int number;
}
