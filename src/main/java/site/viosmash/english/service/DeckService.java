package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.viosmash.english.dto.request.DeckCreateRequest;
import site.viosmash.english.dto.request.DeckUpdateRequest;
import site.viosmash.english.dto.response.DeckResponse;
import site.viosmash.english.entity.Deck;
import site.viosmash.english.entity.Flashcard;
import site.viosmash.english.entity.User;
import site.viosmash.english.mapper.DeckMapper;
import site.viosmash.english.repository.DeckRepository;
import site.viosmash.english.repository.FlashcardRepository;
import site.viosmash.english.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final UserRepository userRepository;
    private final DeckMapper deckMapper;

    // ==========================================
    // 1. CREATE: Tạo Deck và Flashcards đồng thời
    // ==========================================
    @Transactional
    public DeckResponse createDeckWithFlashcards(Integer userId, DeckCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        // 1. Khởi tạo và lưu Deck trước để lấy ID
        Deck deck = new Deck()
                .setUser(user)
                .setTitle(request.getTitle())
                .setDescription(request.getDescription())
                .setCoverImageUrl(request.getCoverImageUrl())
                .setTotalWords(request.getFlashcards() != null ? request.getFlashcards().size() : 0);
        
        deck.setStatus(1); // 1 = Active
        
        Deck savedDeck = deckRepository.save(deck);

        // 2. Map list DTO sang list Entity Flashcard
        if (request.getFlashcards() != null && !request.getFlashcards().isEmpty()) {
            List<Flashcard> flashcards = request.getFlashcards().stream().map(fcDto -> {
                Flashcard fc = new Flashcard()
                        .setDeck(savedDeck) 
                        .setWord(fcDto.getWord())
                        .setPhonetic(fcDto.getPhonetic())
                        .setMeaning(fcDto.getMeaning())
                        .setExampleSentence(fcDto.getExampleSentence())
                        .setVisualCueUrl(fcDto.getVisualCueUrl())
                        .setNote(fcDto.getNote());
                
                fc.setStatus(1); 
                return fc;
            }).collect(Collectors.toList());

            // 3. Lưu toàn bộ Flashcard trong 1 lần gọi (Batch Insert tối ưu hiệu suất)
            flashcardRepository.saveAll(flashcards);
        }

        return deckMapper.toResponse(savedDeck);
    }

    // ==========================================
    // 2. READ: Lấy danh sách hoặc chi tiết
    // ==========================================
    
    // Lấy tất cả Deck đang Active của user
    public List<DeckResponse> getAllActiveDecks(Integer userId) {
        return deckRepository.findByUserIdAndStatus(userId, 1).stream()
                .map(deckMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Lấy chi tiết 1 Deck
    public Deck getDeckById(int deckId) {
        return deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bộ thẻ với ID: " + deckId));
    }

    public DeckResponse getDeckResponseById(Integer userId, int deckId) {
        Deck deck = getDeckById(deckId);
        if (deck.getUser() == null || deck.getUser().getId() != userId) {
            throw new RuntimeException("Không có quyền truy cập bộ thẻ này.");
        }
        return deckMapper.toResponse(deck);
    }

    // ==========================================
    // 3. UPDATE: Cập nhật thông tin cơ bản của Deck
    // ==========================================
    @Transactional
    public DeckResponse updateDeck(Integer userId, int deckId, DeckUpdateRequest request) {
        Deck deck = getDeckById(deckId);
        
        if (deck.getUser() == null || deck.getUser().getId() != userId) {
            throw new RuntimeException("Không có quyền cập nhật bộ thẻ này.");
        }
        
        if (request.getTitle() != null) deck.setTitle(request.getTitle());
        if (request.getDescription() != null) deck.setDescription(request.getDescription());
        if (request.getCoverImageUrl() != null) deck.setCoverImageUrl(request.getCoverImageUrl());
        deck.setStatus(request.getStatus());

        Deck savedDeck = deckRepository.save(deck);

        if (request.getFlashcards() != null) {
            // Get current active flashcards for this deck
            List<Flashcard> existingFlashcards = flashcardRepository.findByDeckIdAndStatus(deckId, 1);
            java.util.Set<Integer> currentCardIds = existingFlashcards.stream()
                    .map(Flashcard::getId)
                    .collect(Collectors.toSet());

            List<Flashcard> cardsToSave = new java.util.ArrayList<>();
            java.util.Set<Integer> updatedCardIds = new java.util.HashSet<>();

            for (site.viosmash.english.dto.request.FlashcardUpdateRequest cardDto : request.getFlashcards()) {
                if (cardDto.getId() != null && currentCardIds.contains(cardDto.getId())) {
                    // Update existing
                    Flashcard existingCard = existingFlashcards.stream()
                            .filter(c -> c.getId() == cardDto.getId().intValue())
                            .findFirst().orElseThrow();
                    
                    existingCard.setWord(cardDto.getWord());
                    existingCard.setPhonetic(cardDto.getPhonetic());
                    existingCard.setMeaning(cardDto.getMeaning());
                    existingCard.setExampleSentence(cardDto.getExampleSentence());
                    existingCard.setVisualCueUrl(cardDto.getVisualCueUrl());
                    existingCard.setNote(cardDto.getNote());
                    
                    if (cardDto.getStatus() != null) {
                        existingCard.setStatus(cardDto.getStatus());
                    } else {
                        existingCard.setStatus(1);
                    }

                    cardsToSave.add(existingCard);
                    updatedCardIds.add(cardDto.getId());
                } else {
                    // Insert new
                    Flashcard newCard = new Flashcard()
                            .setDeck(savedDeck)
                            .setWord(cardDto.getWord())
                            .setPhonetic(cardDto.getPhonetic())
                            .setMeaning(cardDto.getMeaning())
                            .setExampleSentence(cardDto.getExampleSentence())
                            .setVisualCueUrl(cardDto.getVisualCueUrl())
                            .setNote(cardDto.getNote());
                    
                    if (cardDto.getStatus() != null) {
                        newCard.setStatus(cardDto.getStatus());
                    } else {
                        newCard.setStatus(1);
                    }
                    cardsToSave.add(newCard);
                }
            }

            // Soft delete cards not in the update request
            for (Flashcard existingCard : existingFlashcards) {
                if (!updatedCardIds.contains(existingCard.getId())) {
                    existingCard.setStatus(0); // Soft delete
                    cardsToSave.add(existingCard);
                }
            }

            // Batch save
            if (!cardsToSave.isEmpty()) {
                flashcardRepository.saveAll(cardsToSave);
            }
            
            // Update total words count
            long activeCount = cardsToSave.stream().filter(c -> c.getStatus() == 1).count();
            savedDeck.setTotalWords((int) activeCount);
            deckRepository.save(savedDeck);
        }

        return deckMapper.toResponse(savedDeck);
    }

    // ==========================================
    // 4. DELETE: Xóa mềm (Soft Delete)
    // ==========================================
    @Transactional
    public void deleteDeck(int userId, int deckId) {
        Deck deck = getDeckById(deckId);
        if (deck.getUser() == null || deck.getUser().getId() != userId) {
            throw new RuntimeException("Không có quyền xóa bộ thẻ này.");
        }
        // Vì class BaseEntity của bạn có trường status, 
        // ta nên chuyển status về 0 (Inactive/Deleted) thay vì xóa hẳn khỏi DB (Hard Delete)
        // để tránh lỗi cascade khóa ngoại với bảng lịch sử học của User.
        deck.setStatus(0); 
        deckRepository.save(deck);
    }
}