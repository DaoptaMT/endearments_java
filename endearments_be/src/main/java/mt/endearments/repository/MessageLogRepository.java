package mt.endearments.repository;

import mt.endearments.model.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class MessageLogRepository implements JpaRepository<MessageLog, Long> {
}
