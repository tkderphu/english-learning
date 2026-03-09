package site.viosmash.english.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Table(name = "user_topic")
@Entity
@Accessors(chain = true)
public class UserTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "topic_id")
    private int topicId;

    @Column(name = "user_id")
    private int userId;
}
