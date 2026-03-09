package site.viosmash.english.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "author_book")
@Entity
@Accessors(chain = true)
public class AuthorBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "author_id")
    private int authorId;

    @Column(name = "book_id")
    private int bookId;
}
