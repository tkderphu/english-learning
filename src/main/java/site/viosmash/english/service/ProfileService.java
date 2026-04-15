package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.viosmash.english.dto.request.ProfileUpdateRequest;
import site.viosmash.english.dto.response.FileResponse;
import site.viosmash.english.dto.response.ProfileMeResponse;
import site.viosmash.english.entity.User;
import site.viosmash.english.exception.ServiceException;
import site.viosmash.english.repository.LevelRepository;
import site.viosmash.english.repository.UserProfileRepository;
import site.viosmash.english.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final LevelRepository levelRepository;
    private final FileStorageService fileStorageService;

    public ProfileMeResponse getProfile(User user) {
        User fresh = userRepository.findById(user.getId()).orElse(user);
        return buildProfileResponse(fresh);
    }

    @Transactional
    public ProfileMeResponse updateProfile(User user, ProfileUpdateRequest req) {
        // JWT principal only carries a subset of fields; always update from managed entity.
        User managed = userRepository.findById(user.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found"));
        if (req.getFullName() != null) {
            managed.setFullName(req.getFullName().trim());
        }
        if (req.getLocation() != null) {
            managed.setLocation(req.getLocation().trim().isEmpty() ? null : req.getLocation().trim());
        }
        if (req.getLearningLevel() != null) {
            managed.setLearningLevel(req.getLearningLevel().trim().isEmpty() ? null : req.getLearningLevel().trim());
        }
        return buildProfileResponse(userRepository.save(managed));
    }

    @Transactional
    public ProfileMeResponse updateAvatar(User user, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "File is required");
        }
        // JWT principal only carries id/role/email, no password; fetch managed entity to avoid nulling columns.
        User managed = userRepository.findById(user.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found"));
        FileResponse stored = fileStorageService.store(file);
        managed.setAvatar(stored.getUrl());
        return buildProfileResponse(userRepository.save(managed));
    }

    private ProfileMeResponse buildProfileResponse(User user) {
        ProfileMeResponse.ProfileMeResponseBuilder b = ProfileMeResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatar())
                .location(user.getLocation())
                .learningLevel(user.getLearningLevel());

        userProfileRepository.findByUserId(user.getId()).ifPresent(up -> {
            b.levelId(up.getLevelId());
            b.dailyGoalMinutes(up.getDailyGoalMinutes());
            b.learningGoal(up.getLearningGoal());
            b.jobTitle(up.getJobTitle());
            if (up.getLevelId() != null) {
                levelRepository.findById(up.getLevelId()).ifPresent(lv -> b.levelName(lv.getName()));
            }
        });

        return b.build();
    }
}
