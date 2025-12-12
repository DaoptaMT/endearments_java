package mt.endearments.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mt.endearments.enums.RoleType;
import org.hibernate.annotations.ColumnDefault;

import java.util.*;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDTO {
    @NotBlank(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "FULL_NAME_INVALID")
    String name;

    @NotBlank(message = "PASSWORD_INVALID")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "PASSWORD_NOT_FORMAT")
    String password;

    @NotEmpty(message = "ROLE_INVALID")
    List<RoleType> roles;

    Integer otpAttempts;
}
