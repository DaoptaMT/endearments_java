package mt.endearments.service;

import mt.endearments.dto.request.UserRequestDTO;
import mt.endearments.dto.response.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Object getAllUser(int page, int pageSize);
    UserResponseDTO register(UserRequestDTO request);
    void updatePassword(String email, String newPassword);
}
