package site.viosmash.english.groqapi;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class GroqAiConfig {

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.base.url}")
    private String baseUrl;

    @Bean
    public WebClient openAiWebClient() {
        return WebClient.builder()
                .baseUrl(Objects.requireNonNull(baseUrl))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}