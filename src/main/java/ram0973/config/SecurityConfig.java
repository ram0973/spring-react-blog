package ram0973.config;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    protected SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
        http
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint(userAuthenticationEntryPoint)
            )
            .addFilterBefore(new UsernamePasswordAuthenticationFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
            //.addFilterBefore(new CookieAuthFilter(userAuthenticationProvider), UsernamePasswordAuthenticationFilter.class)
            // https://docs.spring.io/spring-security/reference/servlet/authentication/rememberme.html
            .sessionManagement(sessionManagement ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/", "/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/signIn", "/v1/signUp").permitAll()
                //.anyRequest().permitAll()
                .anyRequest().authenticated()
            )
            // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html
            // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-mobile
            // check paths
            .csrf(AbstractHttpConfigurer::disable // TODO remove after testing
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            //.formLogin(AbstractHttpConfigurer::disable)
            // https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html
            //.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
            //    .loginPage("/login")
            //    .usernameParameter("email")
            //)
            .httpBasic((basic) -> basic.securityContextRepository(new HttpSessionSecurityContextRepository()))
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").permitAll()
                .invalidateHttpSession(true)
                .deleteCookies(CookieAuthFilter.COOKIE_NAME) // TODO: check in other places
            );
//        .rememberMe(rememberMe -> rememberMe
//            .useSecureCookie(true)
//            .rememberMeServices()
//        )
        //.passwordManagement(AbstractHttpConfigurer::disable)
        //.httpBasic(AbstractHttpConfigurer::disable)


        //http.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
