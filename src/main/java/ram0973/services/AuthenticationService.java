package ram0973.services;

import org.springframework.stereotype.Service;
import ram0973.dto.CredentialsDTO;
import ram0973.dto.UserDTO;

@Service
public class AuthenticationService {
    public UserDTO findByLogin(String login) {
        return null;
    }

    public UserDTO authenticate(CredentialsDTO credentialsDTO) {
        return null;
    }
}
