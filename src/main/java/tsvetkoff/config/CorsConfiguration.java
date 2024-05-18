package tsvetkoff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author SweetSupremum
 */
@Configuration
public class CorsConfiguration {
    @Value("${creep-tube-ui-domain}")
    private String creepTubeUi;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/build").allowedOrigins(creepTubeUi);
                registry.addMapping("/run").allowedOrigins(creepTubeUi);
                registry.addMapping("/stop").allowedOrigins(creepTubeUi);
            }
        };
    }
}
