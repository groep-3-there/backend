package matchmaker.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(
            cors ->
                cors.configurationSource(
                    request -> {
                      var corss = new CorsConfiguration();
                      corss.setAllowedOrigins(
                          List.of(
                              "http://localhost:3000",
                              "http://161.35.84.133:3000",
                              "http://161.35.84.133",
                              "https://groep-3-there.github.io"));
                      corss.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                      corss.setAllowedHeaders(List.of("*"));
                      return corss;
                    }))
        .authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    //                                .requestMatchers("/aapi/*").authenticated()
                    .anyRequest()
                    .permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    return http.build();
  }
}
