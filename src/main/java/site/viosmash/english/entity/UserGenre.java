package site.viosmash.english.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "user_genre")
@Entity
@Accessors(chain = true)
public class UserGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "genre_id")
    private Integer genreId;
}
