package mt.endearments.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.NotFound;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequestDTO {
    @NotBlank(message = "USERNAME_INVALID")
    String name;

    @NotBlank(message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "EMAIL_INVALID")
    String email;
}
