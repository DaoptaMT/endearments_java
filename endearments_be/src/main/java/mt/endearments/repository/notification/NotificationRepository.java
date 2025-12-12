package mt.endearments.repository.notification;

import mt.endearments.model.Notification;
import mt.endearments.projection.notification.NotificationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = """
            SELECT
              n.[id] AS id,
              n.[content] AS content,
              n.[status] AS status,
              CAST(n.[created_at] AS DATETIME2) AS createdAt,
              n.[user_id] AS userId,
              u.[name] AS name,
              u.[avatar_url] AS avatarUrl
            FROM [endearments].[dbo].[notifications] AS n
            JOIN [endearments].[dbo].[users] AS u
              ON n.[user_id] = u.[id]
            WHERE n.[user_id] = :userId
              AND (:#{#status} IS NULL OR UPPER(n.[status]) = UPPER(:#{#status}))
            ORDER BY n.[created_at] DESC
            """, nativeQuery = true)
    List<NotificationProjection> findNotificationsByFilter(
            @Param("userId") Long userId,
            @Param("status") String status);
}
