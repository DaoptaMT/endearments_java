package mt.endearments.repository;

import mt.endearments.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class NotificationRepository implements JpaRepository<Notification, Long> {
}
