package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "program")
@NoArgsConstructor
@AllArgsConstructor
public class Program extends BaseEntity {
    
    @Column
    private String title;

    private int order;
}