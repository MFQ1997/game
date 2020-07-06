package mai.game.core.config;

import mai.game.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {
    @Autowired
    private SystemConfigService systemConfigService;

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String uploadFolder = systemConfigService.selectAllConfig().get("upload_path").toString();
        factory.setLocation(uploadFolder);
        return factory.createMultipartConfig();
    }
}
