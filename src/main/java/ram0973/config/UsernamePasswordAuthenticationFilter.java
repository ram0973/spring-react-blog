package ram0973.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ram0973.dto.CredentialsDTO;

import java.io.IOException;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter extends OncePerRequestFilter {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final UserAuthenticationProvider userAuthenticationProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if ("/v1/signIn".equals(request.getServletPath()) && HttpMethod.POST.matches(request.getMethod())) {
            try {
                CredentialsDTO credentialsDTO = MAPPER.readValue(request.getInputStream(), CredentialsDTO.class);
                SecurityContextHolder.getContext().setAuthentication(userAuthenticationProvider.validateCredentials(credentialsDTO));
            } catch (RuntimeException e) { //TODO check exception
                SecurityContextHolder.clearContext();
                throw e;
            }
        }
        filterChain.doFilter(request, response);
    }
}
