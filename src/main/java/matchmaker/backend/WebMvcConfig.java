package matchmaker.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Bean
    public AuthInterceptor AuthInterceptorWithBean(){

        return new AuthInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(AuthInterceptorWithBean());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry){

        registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedMethods("*");
        registry.addMapping("/**").allowedOrigins("http://161.35.84.133:3000").allowedMethods("*");
        registry.addMapping("/**").allowedOrigins("http://161.35.84.133").allowedMethods("*");
        registry.addMapping("/**").allowedOrigins("https://groep-3-there.github.io").allowedMethods("*");
    }