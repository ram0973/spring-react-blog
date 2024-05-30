package ram0973.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ram0973.dto.CredentialsDTO;
import ram0973.dto.UserDTO;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Value("${auth.cookie.hmac-key:secret-key}")
    private String secretKey;

    private final PasswordEncoder passwordEncoder;

    public UserDTO findByLogin(String login) {
        return null;
    }

    public UserDTO authenticate(CredentialsDTO credentialsDto) {
        String encodedMasterPassword = passwordEncoder.encode(CharBuffer.wrap("the-password"));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.credentials()), encodedMasterPassword)) {
            return new UserDTO(1L, "Sergio", "Lema", "login", "token");
        }
        throw new RuntimeException("Invalid password");
    }

    public String createToken(UserDTO user) {
        return user. + "&" + user.getLogin() + "&" + calculateHmac(user);
    }

    public UserDTO findByToken(String token) {
        String[] parts = token.split("&");

        Long userId = Long.valueOf(parts[0]);
        String login = parts[1];
        String hmac = parts[2];

        UserDTO UserDTO = findByLogin(login);

        if (!hmac.equals(calculateHmac(UserDTO)) || userId != UserDTO.getId()) {
            throw new RuntimeException("Invalid Cookie value");
        }

        return UserDTO;
    }


    private String calculateHmac(UserDTO user) {
        byte[] secretKeyBytes = Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = Objects.requireNonNull(user.getId() + "&" + user.getLogin()).getBytes(StandardCharsets.UTF_8);

        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(valueBytes);
            return Base64.getEncoder().encodeToString(hmacBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
