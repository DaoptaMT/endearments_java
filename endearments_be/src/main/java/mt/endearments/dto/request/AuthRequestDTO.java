package mt.endearments.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequestDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public AuthRequestDTO() {}

    public AuthRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
