package mt.endearments.controller.notification;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mt.endearments.dto.response.notification.NotificationResponse;
import mt.endearments.service.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByFilters(
            @PathVariable Long userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        List<NotificationResponse> notes = notificationService.getNotifications(userId, status);
        return ResponseEntity.ok(notes);
    }
}
