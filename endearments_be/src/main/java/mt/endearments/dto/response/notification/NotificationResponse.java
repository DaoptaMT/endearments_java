package mt.endearments.dto.response.notification;

import lombok.Builder;
import lombok.Data;
import mt.endearments.dto.response.blogList.UserDto;

import java.time.Instant;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String content;
    private String status;
    private Instant createdAt;
    private UserDto user;
}