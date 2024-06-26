package ram0973.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.http.HttpHeaders;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private static final Long MAX_AGE = 3600L;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
            .allowedHeaders(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
            )
            .allowedMethods(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()
            )
            .maxAge(MAX_AGE)
            .allowedOrigins("*")
            .allowCredentials(true);
    }
}
