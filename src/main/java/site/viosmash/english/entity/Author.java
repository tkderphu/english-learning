package site.viosmash.english.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "author")
@Entity
@Accessors(chain = true)
public class Author extends BaseEntity {

    private String name;

    private String avatar;

    private String nationality;

    private String biography;
}
