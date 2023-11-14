package matchmaker.backend;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

        registry.addMapping("/**").allowedOrigins("http://localhost:3000","http://161.35.84.133:3000", "http://161.35.84.133","https://groep-3-there.github.io").allowedMethods("*");


    }


}