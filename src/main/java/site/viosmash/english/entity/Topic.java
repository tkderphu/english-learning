package site.viosmash.english.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "topic")
@Entity
@Accessors(chain = true)
public class Topic extends BaseEntity {

    private String name;

    private String description;

    private String thumbnail;
}
