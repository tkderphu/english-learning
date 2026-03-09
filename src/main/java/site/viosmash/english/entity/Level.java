package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "level")
@Entity
@Accessors(chain = true)
public class Level extends BaseEntity {

    private String name;

    private String description;

    @Column(name = "number_course")
    private int numberCourse;
}
