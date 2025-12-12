package mt.endearments.controller;

import jakarta.validation.Valid;
import mt.endearments.dto.request.UpdateMessageRequestDTO;
import mt.endearments.dto.response.MessageDetailResponseDTO;
import mt.endearments.dto.response.MessageResponseDTO;
import mt.endearments.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/list")
    public ResponseEntity<List<MessageResponseDTO>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDetailResponseDTO> getMessageDetail(@PathVariable Long id) {
        MessageDetailResponseDTO detail = messageService.getMessageDetail(id);
        return ResponseEntity.ok(detail);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageDetailResponseDTO> updateMessage(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMessageRequestDTO dto,
            @RequestParam Long userId
    ) {
        MessageDetailResponseDTO updated = messageService.updateMessage(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        messageService.softDeleteMessage(id, userId);
        return ResponseEntity.noContent().build();
    }
}
