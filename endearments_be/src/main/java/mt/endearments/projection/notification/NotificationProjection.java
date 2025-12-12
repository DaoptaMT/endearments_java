package mt.endearments.projection.notification;

import java.time.Instant;

public interface NotificationProjection {
    Long getId();

    String getContent();

    String getStatus();

    Instant getCreatedAt();

    Long getUserId();

    String getName();

    String getAvatarUrl();
}
