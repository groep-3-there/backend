package matchmaker.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Bean
  public AuthInterceptor AuthInterceptorWithBean() {

    return new AuthInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(AuthInterceptorWithBean());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry
        .addMapping("/**")
        .allowedOrigins(
            "http://localhost:3000",
            "http://161.35.84.133:3000",
            "http://161.35.84.133",
            "http://matchmakergroep3.nl",
            "http://www.matchmakergroep3.nl",
            "https://www.matchmakergroep3.nl",
            "https://matchmakergroep3.nl",
            "https://groep-3-there.github.io")
        .allowedMethods("GET,POST,PUT,DELETE,OPTIONS");
  }
}
