package mai.game.core.config;

import mai.game.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("*").maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //WINDOWS版
        //registry.addResourceHandler("/img/**").addResourceLocations("file:D:/web/img/");
        //LINUX版
        //registry.addResourceHandler("/img/**").addResourceLocations("file:/usr/local/img/");

        String uploadFolder = systemConfigService.selectAllConfig().get("upload_path").toString();
        String staticAccessPath = systemConfigService.selectAllConfig().get("static_access_path").toString();
        registry.addResourceHandler(staticAccessPath+"**").addResourceLocations("file:" + uploadFolder);
    }
}
