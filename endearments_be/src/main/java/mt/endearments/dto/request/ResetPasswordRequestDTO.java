package mt.endearments.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordRequestDTO {

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;

    public ResetPasswordRequestDTO() {}

    public ResetPasswordRequestDTO(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }
}
