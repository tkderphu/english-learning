package site.viosmash.english.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "flashcards")
@Entity
@Accessors(chain = true)
public class Flashcard extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @Column(nullable = false)
    private String term;

    @Column(columnDefinition = "TEXT")
    private String definition;

    @Column(name = "image_url")
    private String imageUrl;
}