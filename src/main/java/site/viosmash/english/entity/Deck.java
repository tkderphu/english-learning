package site.viosmash.english.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "decks")
@Entity
@Accessors(chain = true)
public class Deck extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "total_words")
    private Integer totalWords = 0;

    @jakarta.persistence.OneToMany(mappedBy = "deck", cascade = jakarta.persistence.CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Flashcard> flashcards = new java.util.ArrayList<>();
}