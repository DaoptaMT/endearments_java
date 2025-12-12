package mt.endearments.service;

import mt.endearments.dto.request.UserRequestDTO;
import mt.endearments.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UsersRepository usersRepository;

    private void login(UserRequestDTO userRequest) {

    }
}
