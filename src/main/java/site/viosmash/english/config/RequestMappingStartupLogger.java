package site.viosmash.english.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Ghi log các mapping chứa "profile" khi start — nếu không có dòng nào, ProfileController chưa được nạp (build/restart).
 */
@Slf4j
@Component
@Profile("!migration")
@RequiredArgsConstructor
public class RequestMappingStartupLogger implements ApplicationRunner {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void run(ApplicationArguments args) {
        requestMappingHandlerMapping.getHandlerMethods().forEach((info, method) -> {
            String s = info.toString();
            if (s.contains("profile")) {
                log.info("Mapped {} -> {}.{}", s, method.getBeanType().getSimpleName(), method.getMethod().getName());
            }
        });
    }
}
