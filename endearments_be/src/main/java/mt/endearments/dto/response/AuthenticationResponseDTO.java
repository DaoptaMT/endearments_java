package mt.endearments.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import mt.endearments.enums.TokenType;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponseDTO {
    @Enumerated(EnumType.STRING)
    TokenType tokenType;
    Long id;
    String email;
    List<String> roles;
    String message;
    @JsonProperty("accessToken")
    String accessToken;
    @JsonProperty("refreshToken")
    String refreshToken;
}
