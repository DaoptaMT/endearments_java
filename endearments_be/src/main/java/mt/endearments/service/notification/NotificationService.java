package mt.endearments.service.notification;

import mt.endearments.dto.response.notification.NotificationResponse;
import mt.endearments.projection.notification.NotificationProjection;

import java.util.List;

public interface NotificationService {

    NotificationResponse mapToNotificationResponse(NotificationProjection notification);

    List<NotificationResponse> getNotifications(Long userId, String status);
}
