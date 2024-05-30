package ram0973.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import ram0973.dto.CredentialsDTO;
import ram0973.dto.UserDTO;
import ram0973.services.AuthenticationService;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationService authenticationService;

    public Authentication validateCredentials(CredentialsDTO credentialsDTO) {
        UserDTO userDTO = authenticationService.authenticate(credentialsDTO);
        return new UsernamePasswordAuthenticationToken(userDTO, null, Collections.emptyList());
    }

    public Authentication validateToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
        String login = claims.getSubject();
        UserDTO userDTO = authenticationService.findByLogin(login);
        return new UsernamePasswordAuthenticationToken(userDTO, null, Collections.emptyList());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDTO userDTO = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            userDTO = authenticationService.authenticate(
                new CredentialsDTO((String) authentication.getPrincipal(), (char[]) authentication.getCredentials()));
        } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            authenticationService.findByToken((String) authentication.getPrincipal());
        }
        if (userDTO == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userDTO, null, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
