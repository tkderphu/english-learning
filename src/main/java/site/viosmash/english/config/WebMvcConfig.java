package site.viosmash.english.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.storage.path:uploads}")
    private String storagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            Path baseDir = Path.of(storagePath).toAbsolutePath();

            // Dùng toUri().toString() để Java tự chuyển thành chuẩn file:///.../
            String location = baseDir.toUri().toString();

            // Đảm bảo kết thúc bằng dấu /
            if (!location.endsWith("/")) {
                location += "/";
            }

            // Map URL /api/assets/** vào thư mục vật lý
            registry.addResourceHandler("/api/assets/**")
                    .addResourceLocations(location);

        } catch (Exception ex) {
            // Log lỗi ra để dễ debug nếu có vấn đề
            ex.printStackTrace();
        }
    }
}