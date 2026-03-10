package site.viosmash.english.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "book_genre")
@Entity
@Accessors(chain = true)
public class BookGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "genre_id")
    private Integer genreId;

    @Column(name = "book_id")
    private Integer bookId;
}
