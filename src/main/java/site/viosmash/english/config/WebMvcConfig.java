package site.viosmash.english.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.storage.path:uploads}")
    private String storagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            Path baseDir = Path.of(storagePath).toAbsolutePath();
            String location = "file:" + baseDir.toString() + File.separator;

            // Expose the uploaded files under the /uploads/** URL path
            registry.addResourceHandler("/api/assets/**")
                    .addResourceLocations(location);
        } catch (Exception ex) {
            // If something goes wrong, don't block app startup; resources won't be served.
        }
    }
}
