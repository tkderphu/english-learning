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
    private String word;

    private String phonetic;

    @Column(name = "part_of_speech")
    private String partOfSpeech;

    @Column(columnDefinition = "TEXT")
    private String meaning;

    @Column(name = "example_sentence", columnDefinition = "TEXT")
    private String exampleSentence;

    @Column(name = "visual_cue_url")
    private String visualCueUrl;

    @Column(columnDefinition = "TEXT")
    private String note;
}