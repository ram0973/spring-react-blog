package ram0973.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
public class UserAuthenticationProvider {

    private final AuthenticationService authenticationService;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secret;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(String login) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1));
        return Jwts.builder()
            .issuer("ram0973") //TODO: refactor
            .claims()
            .issuedAt(new Date())
            .subject(login)
            .expiration(expirationDate)
            .and()
            .signWith(secretKey)
            .compact();
    }

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
}
