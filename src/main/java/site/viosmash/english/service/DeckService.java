package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.viosmash.english.dto.request.DeckCreateRequest;
import site.viosmash.english.dto.request.DeckStudyCompleteRequest;
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

/**
 * Service xử lý các nghiệp vụ liên quan đến Bộ thẻ (Deck) và Flashcard.
 * Đóng vai trò là lớp nghiệp vụ trung tâm kết nối giữa Controller và Repository.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeckService {

    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final UserRepository userRepository;
    private final DeckMapper deckMapper;
    private final ProfileLearningActivityService profileLearningActivityService;

    // ==========================================
    // 1. CREATE: Tạo Deck và Flashcards đồng thời
    // ==========================================

    /**
     * Tạo một bộ thẻ mới kèm theo danh sách các thẻ từ vựng (nếu có).
     * Hàm này thực thi trong một transaction duy nhất.
     *
     * @param userId  ID của người dùng tạo bộ thẻ.
     * @param request Dữ liệu đầu vào để tạo bộ thẻ.
     * @return DTO chứa thông tin bộ thẻ vừa tạo thành công.
     */
    @Transactional
    public DeckResponse createDeckWithFlashcards(Integer userId, DeckCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        // 1. Khởi tạo và lưu Deck trước để lấy ID
        Deck deck = new Deck()
                .setUser(user)
                .setTitle(request.getTitle());

        deck.setStatus(1); // 1 = Active
        
        Deck savedDeck = deckRepository.save(deck);

        // 2. Map list DTO sang list Entity Flashcard
        if (request.getFlashcards() != null && !request.getFlashcards().isEmpty()) {
            List<Flashcard> flashcards = request.getFlashcards().stream().map(fcDto -> {
                Flashcard fc = new Flashcard()
                        .setDeck(savedDeck) 
                        .setTerm(fcDto.getTerm())
                        .setDefinition(fcDto.getDefinition())
                        .setImageUrl(fcDto.getImageUrl());
                
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
    
    /**
     * Lấy danh sách toàn bộ các bộ thẻ đang hoạt động của một người dùng.
     * Hỗ trợ lọc theo từ khóa tìm kiếm.
     *
     * @param userId      ID của người dùng cần lấy bộ thẻ.
     * @param searchParam (Tuỳ chọn) Từ khóa tìm kiếm tiêu đề bộ thẻ.
     * @return Danh sách các bộ thẻ dưới dạng DTO.
     */
    public List<DeckResponse> getAllActiveDecks(Integer userId, String searchParam) {
        return deckRepository.findByUserIdAndStatusAndTitleContainingIgnoreCase(userId, 1, searchParam).stream()
                .map(deckMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Tìm bộ thẻ theo ID trong cơ sở dữ liệu. 
     * Ném ngoại lệ nếu không tìm thấy.
     *
     * @param deckId ID của bộ thẻ.
     * @return Entity Deck chứa thông tin bộ thẻ.
     */
    public Deck getDeckById(int deckId) {
        return deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bộ thẻ với ID: " + deckId));
    }

    /**
     * Lấy thông tin chi tiết của một bộ thẻ kèm kiểm tra quyền sở hữu.
     *
     * @param userId ID người dùng yêu cầu (dùng để xác thực quyền).
     * @param deckId ID bộ thẻ cần lấy.
     * @return DTO chứa thông tin chi tiết bộ thẻ.
     * @throws RuntimeException nếu người dùng không có quyền truy cập.
     */
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

    /**
     * Cập nhật thông tin tiêu đề bộ thẻ và danh sách flashcard (thêm mới, cập nhật, hoặc xóa mềm).
     *
     * @param userId  ID người dùng đang thực hiện cập nhật.
     * @param deckId  ID của bộ thẻ cần cập nhật.
     * @param request Dữ liệu cập nhật.
     * @return DTO chứa thông tin bộ thẻ sau khi đã cập nhật thành công.
     */
    @Transactional
    public DeckResponse updateDeck(Integer userId, int deckId, DeckUpdateRequest request) {
        Deck deck = getDeckById(deckId);
        
        if (deck.getUser() == null || deck.getUser().getId() != userId) {
            throw new RuntimeException("Không có quyền cập nhật bộ thẻ này.");
        }
        
        if (request.getTitle() != null) deck.setTitle(request.getTitle());
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
                    
                    existingCard.setTerm(cardDto.getTerm());
                    existingCard.setDefinition(cardDto.getDefinition());
                    existingCard.setImageUrl(cardDto.getImageUrl());
                    
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
                            .setTerm(cardDto.getTerm())
                            .setDefinition(cardDto.getDefinition())
                            .setImageUrl(cardDto.getImageUrl());
                    
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
            
            // No total words count update needed
            deckRepository.save(savedDeck);
        }

        return deckMapper.toResponse(savedDeck);
    }

    // ==========================================
    // 4. DELETE: Xóa mềm (Soft Delete)
    // ==========================================

    /**
     * Thực hiện xóa mềm một bộ thẻ. Chuyển trạng thái bộ thẻ về 0 (Inactive) 
     * thay vì xóa vật lý khỏi database để tránh lỗi khóa ngoại đối với thống kê.
     *
     * @param userId ID của người dùng.
     * @param deckId ID của bộ thẻ cần xóa.
     */
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

    /**
     * Ghi nhận việc hoàn thành một phiên học flashcard.
     * Gọi qua service Activity để ghi log và tính điểm heatmap.
     *
     * @param userId ID người dùng hoàn thành học.
     * @param deckId ID của bộ thẻ vừa học.
     * @param req    Dữ liệu báo cáo (thời gian học, điểm số).
     */
    @Transactional
    public void recordStudySessionComplete(Integer userId, int deckId, DeckStudyCompleteRequest req) {
        Deck deck = getDeckById(deckId);
        if (deck.getUser() == null || deck.getUser().getId() == null || deck.getUser().getId() != userId) {
            throw new RuntimeException("Không có quyền truy cập bộ thẻ này.");
        }
        try {
            profileLearningActivityService.logFlashcardDeckStudyCompleted(
                    userId,
                    deckId,
                    deck.getTitle(),
                    req.getDurationSeconds(),
                    req.getCardsReviewed(),
                    req.getQuizCorrect(),
                    req.getQuizTotal()
            );
        } catch (Exception ex) {
            log.warn("Could not record learning activity for deck study {}", deckId, ex);
        }
    }
}