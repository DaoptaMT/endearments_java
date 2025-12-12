package mt.endearments.service;

import jakarta.servlet.http.HttpServletRequest;
import mt.endearments.dto.request.AuthenticationRequestDTO;
import mt.endearments.dto.response.AuthenticationResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO);

    AuthenticationResponseDTO refreshToken(HttpServletRequest request);
}
