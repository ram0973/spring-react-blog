package ram0973.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieAuthFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME = "JSESSION";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        Optional<Cookie> cookieAuth = Stream.of(Optional.ofNullable(request.getCookies())
            .orElse(new Cookie[0]))
            .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
            .findFirst();
        cookieAuth.ifPresent(cookie -> SecurityContextHolder.getContext().setAuthentication(
            new PreAuthenticatedAuthenticationToken(cookie.getValue(), null))
        );
        filterChain.doFilter(request, response);
    }
}
