package mt.endearments.repository.messages.interfaceMessageRepo;

import mt.endearments.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message ,Long> {
    Optional<Message> findByIdAndDeletedAtIsNull(Long id);
    List<Message> findAllBySenderIdAndDeletedAtIsNull(Long senderId);
    List<Message> findBySenderId(Long senderId);
    List<Message> findByApprovalStatus(String status);
}
