package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.viosmash.english.dto.request.LevelCreateRequest;
import site.viosmash.english.dto.response.LevelResponse;
import site.viosmash.english.dto.response.PageResponse;
import site.viosmash.english.entity.Level;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.LevelRepository;
import site.viosmash.english.util.Util;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelService {

    private final LevelRepository levelRepository;

    private final site.viosmash.english.repository.UserProfileRepository userProfileRepository;

    private final Util util;

    public PageResponse<LevelResponse> page(int page, int limit, String keyword) {
        String kw = (keyword == null || keyword.isBlank()) ? null : "%" + keyword.toLowerCase() + "%";
        return util.convert(levelRepository.findAllByKeyword(PageRequest.of(page - 1, limit), kw));
    }

    public List<LevelResponse> getLevels() {
        return levelRepository.findAllLevels();
    }

    public LevelResponse create(LevelCreateRequest req) {
        Level l = new Level();
        l.setName(req.getName());
        l.setDescription(req.getDescription());
        l.setNumberCourse(req.getNumberCourse());
        l.setStatus(1);
        l = levelRepository.save(l);
        return new LevelResponse(l.getId(), l.getName(), l.getDescription(), l.getNumberCourse(), l.getStatus());
    }

    @org.springframework.transaction.annotation.Transactional
    public Boolean updateUserLevel(site.viosmash.english.dto.request.UpdateUserLevelRequest req) {
        Integer userId = util.getCurrentUser().getId();
        site.viosmash.english.entity.UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> new site.viosmash.english.entity.UserProfile().setUserId(userId));
        profile.setLevelId(req.getLevelId());
        userProfileRepository.save(profile);
        return true;
    }
}
