package mt.endearments.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {
    @NotNull(message = "không được để trống")
    private Long senderId;

    @NotBlank(message = "không được để trống")
    @Pattern(regexp = "^(text|image|voice|music)$", message = "phải là text, image, voice hoặc music")
    private String messageType;

    @NotNull(message = "không được để trống")
    private Instant createdAt;
}
