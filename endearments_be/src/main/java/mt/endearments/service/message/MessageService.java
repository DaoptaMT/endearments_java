package mt.endearments.service.message;

import jakarta.transaction.Transactional;
import mt.endearments.dto.request.UpdateMessageRequestDTO;
import mt.endearments.dto.response.MessageDetailResponseDTO;
import mt.endearments.dto.response.MessageResponseDTO;
import mt.endearments.exception.ForbiddenException;
import mt.endearments.exception.ResourceNotFoundException;
import mt.endearments.model.Message;
import mt.endearments.repository.messages.interfaceMessageRepo.MessageRepository;
import mt.endearments.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    private FileStorageService fileStorageService;

    /**
     * Auth: DaoMT
     * Create Date: 25/09/2025
     * Description: Create message with test, voice, image, music.
     *
     * @param message message
     * @param image   image
     * @param voice   voice
     * @param music   music
     * @return new object message
     */
    @Transactional
    public Message createMessage(Message message,
                                 MultipartFile image,
                                 MultipartFile voice,
                                 MultipartFile music) {

        boolean hasContent = false;

        if (message.getMessageText() != null && !message.getMessageText().isEmpty()) {
            hasContent = true;
        }
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.upload(image);
            message.setImageUrl(imageUrl);
            hasContent = true;
        }
        if (voice != null && !voice.isEmpty()) {
            String voiceUrl = fileStorageService.upload(voice);
            message.setVoiceUrl(voiceUrl);
            hasContent = true;
        }
        if (music != null && !music.isEmpty()) {
            String musicUrl = fileStorageService.upload(music);
            message.setMusicUrl(musicUrl);
            hasContent = true;
        }

        if (!hasContent) {
            throw new IllegalArgumentException("Tin nhắn phải có ít nhất một nội dung: văn bản, hình ảnh, giọng nói hoặc nhạc.");
        }

        message.setApprovalStatus("pending");
        message.setCreatedAt(Instant.now());
        message.setUpdatedAt(Instant.now());

        return messageRepository.save(message);
    }

    public List<Message> getUserMessages(Long senderId) {
        return messageRepository.findAllBySenderIdAndDeletedAtIsNull(senderId);
    }

    public List<Message> getPendingMessages() {
        return messageRepository.findByApprovalStatus("pending");
    }

    public List<MessageResponseDTO> getAllMessages() {
        return messageRepository.findAll().stream()
                .filter(msg -> msg.getDeletedAt() == null)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Message approveMessage(Long messageId) {
        Message msg = messageRepository.findByIdAndDeletedAtIsNull(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        msg.setApprovalStatus("approved");
        msg.setApprovedAt(Instant.now());
        msg.setSentAt(Instant.now());
        return messageRepository.save(msg);
    }

    @Transactional
    public Message rejectMessage(Long messageId, String reason) {
        Message msg = messageRepository.findByIdAndDeletedAtIsNull(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        msg.setApprovalStatus("rejected");
        msg.setRejectionReason(reason);
        msg.setApprovedAt(Instant.now());
        return messageRepository.save(msg);
    }

    @Transactional
    public MessageDetailResponseDTO updateMessage(Long id, UpdateMessageRequestDTO dto, Long currentUserId) {
        Message message = messageRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!message.getSenderId().equals(currentUserId)) {
            throw new ForbiddenException("Bạn không có quyền cập nhật tin nhắn này");
        }

        message.setMessageText(dto.getMessageText());
        message.setImageUrl(dto.getImageUrl());
        message.setVoiceUrl(dto.getVoiceUrl());
        message.setMusicUrl(dto.getMusicUrl());
        message.setPrivateNote(dto.getPrivateNote());
        message.setMessageType(dto.getMessageType());

        message.setUpdatedAt(Instant.now());

        Message saved = messageRepository.save(message);
        return mapToDetailDTO(saved);
    }

    @Transactional
    public Message updateStatus(Long messageId, String status, String rejectionReason) {
        Message message = messageRepository.findByIdAndDeletedAtIsNull(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        message.setApprovalStatus(status);
        message.setUpdatedAt(Instant.now());

        if ("approved".equalsIgnoreCase(status)) {
            message.setApprovedAt(Instant.now());
            message.setSentAt(Instant.now());
        }
        if ("rejected".equalsIgnoreCase(status)) {
            message.setRejectionReason(rejectionReason);
        }

        return messageRepository.save(message);
    }

    @Transactional
    public void softDeleteMessage(Long id, Long currentUserId) {
        Message message = messageRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!message.getSenderId().equals(currentUserId)) {
            throw new ForbiddenException("Bạn không có quyền xóa tin nhắn này");
        }

        message.setDeletedAt(Instant.now());
        messageRepository.save(message);
    }

    public MessageDetailResponseDTO getMessageDetail(Long messageId) {
        Message message = messageRepository.findByIdAndDeletedAtIsNull(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        return mapToDetailDTO(message);
    }
    
    private MessageResponseDTO toDTO(Message message) {
        return MessageResponseDTO.builder()
                .senderId(message.getSenderId())
                .messageType(message.getMessageType())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private MessageDetailResponseDTO mapToDetailDTO(Message message) {
        return MessageDetailResponseDTO.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .recipientEmail(message.getRecipientEmail())
                .recipientPhone(message.getRecipientPhone())
                .recipientZalo(message.getRecipientZalo())
                .messageType(message.getMessageType())
                .messageText(message.getMessageText())
                .imageUrl(message.getImageUrl())
                .voiceUrl(message.getVoiceUrl())
                .musicUrl(message.getMusicUrl())
                .isAnonymous(message.getIsAnonymous())
                .approvalStatus(message.getApprovalStatus())
                .rejectionReason(message.getRejectionReason())
                .approvedAt(message.getApprovedAt())
                .receiverName(message.getReceiverName())
                .privateNote(message.getPrivateNote())
                .sentAt(message.getSentAt())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
