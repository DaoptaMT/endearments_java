package mt.endearments.repository.messages.interfaceMessageRepo;

import mt.endearments.model.MessageLog;

import java.util.List;

public interface MessageLogRepository {
    List<MessageLog> findByMessageId(Long messageId);
    List<MessageLog> findByChannel(String channel);
    List<MessageLog> findByStatus(String status);
}
