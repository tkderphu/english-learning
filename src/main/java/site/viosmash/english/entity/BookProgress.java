package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Table(name = "book_progress")
@Entity
@Accessors(chain = true)
public class BookProgress extends BaseEntity {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "progress_percent")
    private double progressPercent;

    @Column(name = "last_read")
    private LocalDateTime lastRead;

    @Column(name = "is_favorite")
    private Integer isFavorite;
}
