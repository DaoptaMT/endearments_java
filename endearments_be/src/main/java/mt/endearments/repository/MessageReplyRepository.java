package mt.endearments.repository;

import mt.endearments.model.MessageReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class MessageReplyRepository implements JpaRepository<MessageReply, Long> {
}
