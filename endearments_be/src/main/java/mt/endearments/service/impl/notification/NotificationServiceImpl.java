package mt.endearments.service.impl.notification;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mt.endearments.dto.response.blogList.UserDto;
import mt.endearments.dto.response.notification.NotificationResponse;
import mt.endearments.projection.notification.NotificationProjection;
import mt.endearments.repository.notification.NotificationRepository;
import mt.endearments.service.notification.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;

    @Override
    public NotificationResponse mapToNotificationResponse(NotificationProjection note) {
        return NotificationResponse.builder()
                .id(note.getId())
                .content(note.getContent())
                .status(note.getStatus())
                .createdAt(note.getCreatedAt())
                .user(UserDto.builder()
                        .id(note.getUserId())
                        .name(note.getName())
                        .avatarUrl(note.getAvatarUrl())
                        .build())
                .build();
    }

    @Override
    public List<NotificationResponse> getNotifications(Long userId, String status) {
        List<NotificationProjection> entities = notificationRepository.findNotificationsByFilter(userId, status);

        return entities.stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());
    }
}
