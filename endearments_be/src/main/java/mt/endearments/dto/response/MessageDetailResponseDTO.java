package mt.endearments.dto.response;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDetailResponseDTO {
    @NotNull(message = "không được để trống")
    private Long id;

    @NotNull(message = "không được để trống")
    private Long senderId;

    @Email(message = "phải là email hợp lệ")
    private String recipientEmail;

    @Pattern(
            regexp = "^(0|\\+84)[0-9]{9,10}$",
            message = "phải là số điện thoại hợp lệ"
    )
    private String recipientPhone;

    private String recipientZalo;

    @NotBlank(message = "không được để trống")
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

    @NotNull(message = "không được để trống")
    private Boolean isAnonymous;

    @NotBlank(message = "không được để trống")
    @Pattern(
            regexp = "^(pending|approved|rejected)$",
            message = "phải là pending, approved hoặc rejected"
    )
    private String approvalStatus;

    @Size(max = 1000, message = "không được vượt quá 1000 ký tự")
    private String rejectionReason;

    private Instant approvedAt;

    @Size(max = 100, message = "không được vượt quá 100 ký tự")
    private String receiverName;

    @Size(max = 1000, message = "không được vượt quá 1000 ký tự")
    private String privateNote;

    private Instant sentAt;

    @NotNull(message = "không được để trống")
    private Instant createdAt;

    private Instant updatedAt;
}
