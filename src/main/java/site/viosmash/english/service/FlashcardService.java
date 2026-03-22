package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.viosmash.english.entity.Flashcard;
import site.viosmash.english.repository.FlashcardRepository;
import site.viosmash.english.dto.response.FlashcardStudyDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;

    /**
     * Lấy TẤT CẢ flashcard active của một bộ thẻ (không lọc theo ngày ôn tập)
     * Dùng cho chế độ luyện đơn giản kiểu Quizlet — mỗi lần vào đều học từ đầu
     */
    public List<FlashcardStudyDTO> getAllFlashcardsInDeck(int deckId) {
        List<Flashcard> flashcards = flashcardRepository.findByDeckIdAndStatus(deckId, 1);
        return flashcards.stream()
                .map(flashcard -> new FlashcardStudyDTO(
                        flashcard.getId(),
                        flashcard.getWord(),
                        flashcard.getPhonetic(),
                        flashcard.getPartOfSpeech(),
                        flashcard.getMeaning(),
                        flashcard.getExampleSentence(),
                        flashcard.getVisualCueUrl()
                ))
                .collect(Collectors.toList());
    }
}