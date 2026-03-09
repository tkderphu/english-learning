package site.viosmash.english.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "genre")
@Entity
@Accessors(chain = true)
public class Genre extends BaseEntity {

    private String name;

    private String thumbnail;

    private String description;
}
