package site.viosmash.english;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootTest
@ActiveProfiles("test")
class EnglishApplicationTests {

	@DynamicPropertySource
	static void jwtSecretForTests(DynamicPropertyRegistry registry) {
		String b64 = Base64.getEncoder().encodeToString(
				"english-app-test-jwt-secret-not-for-production".getBytes(StandardCharsets.UTF_8));
		registry.add("spring.security.jwt.secret-key", () -> b64);
	}

	@Test
	void contextLoads() {
	}

}
