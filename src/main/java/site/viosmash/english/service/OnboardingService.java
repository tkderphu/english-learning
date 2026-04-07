package site.viosmash.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.viosmash.english.entity.UserProfile;
import site.viosmash.english.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public boolean hasCompletedAfterLogin(Integer userId) {
        return userProfileRepository
                .findByUserId(userId)
                .map(UserProfile::isAfterLoginOnboardingCompleted)
                .orElse(false);
    }

    @Transactional
    public boolean setAfterLoginOnboardingCompleted(Integer userId, boolean completed) {
        UserProfile profile = userProfileRepository
                .findByUserId(userId)
                .orElseGet(() -> new UserProfile().setUserId(userId));
        profile.setAfterLoginOnboardingCompleted(completed);
        userProfileRepository.save(profile);
        return completed;
    }
}
