package mt.endearments.mapper;

import mt.endearments.dto.request.UserRequestDTO;
import mt.endearments.dto.response.UserResponseDTO;
import mt.endearments.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequestDTO userRequestDTO);

    UserResponseDTO toUserResponseDTO(User user);
}
