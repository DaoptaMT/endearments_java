package mt.endearments.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMessageRequestDTO {

    @Pattern(
            regexp = "^(text|image|voice|music)$",
            message = "phải là text, image, voice hoặc music"
    )
    private String messageType;

    @Size(max = 2000, message = "không được vượt quá 2000 ký tự")
    private String messageText;

    @Pattern(
            regexp = "^(https?://.*)?$",
            message = "phải là URL hợp lệ hoặc để trống"
    )
    private String imageUrl;

    @Pattern(
            regexp = "^(https?://.*)?$",
            message = "phải là URL hợp lệ hoặc để trống"
    )
    private String voiceUrl;

    @Pattern(
            regexp = "^(https?://.*)?$",
            message = "phải là URL hợp lệ hoặc để trống"
    )
    private String musicUrl;

    @Size(max = 1000, message = "không được vượt quá 1000 ký tự")
    private String privateNote;
}

