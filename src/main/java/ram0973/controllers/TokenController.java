package ram0973.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * A controller for the token resource.
 *
 * @author Josh Cummings
 */
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtEncoder encoder;
    private static final long TOKEN_EXPIRY_IN_SECONDS = 3600L; // seconds

    @PostMapping("/token")
    public String token(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(TOKEN_EXPIRY_IN_SECONDS))
            .subject(authentication.getName())
            .claim("scope", scope)
            .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
