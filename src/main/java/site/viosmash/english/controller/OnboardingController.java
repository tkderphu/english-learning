package site.viosmash.english.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.viosmash.english.dto.response.BaseResponse;
import site.viosmash.english.service.OnboardingService;
import site.viosmash.english.util.Util;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
@Tag(name = "Onboarding", description = "After-login onboarding status")
public class OnboardingController {

    private final OnboardingService onboardingService;
    private final Util util;

    @Operation(summary = "Whether the current user completed after-login onboarding", security = {
            @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/v1/after-login")
    public ResponseEntity<BaseResponse<Boolean>> hasCompletedAfterLoginOnboarding() {
        Integer userId = util.getCurrentUser().getId();
        boolean value = onboardingService.hasCompletedAfterLogin(userId);
        return ResponseEntity.ok(BaseResponse.success(value));
    }

    @Operation(summary = "Set after-login onboarding completed flag", security = {
            @SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/v1/update/{completed}")
    public ResponseEntity<BaseResponse<Boolean>> setAfterLoginOnboardingCompleted(
            @PathVariable boolean completed) {
        Integer userId = util.getCurrentUser().getId();
        boolean value = onboardingService.setAfterLoginOnboardingCompleted(userId, completed);
        return ResponseEntity.ok(BaseResponse.success(value));
    }
}
