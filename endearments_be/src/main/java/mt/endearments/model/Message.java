package mt.endearments.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "messages", schema = "dbo")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Nationalized
    @Column(name = "recipient_email")
    private String recipientEmail;

    @Nationalized
    @Column(name = "recipient_phone", length = 20)
    private String recipientPhone;

    @Nationalized
    @Column(name = "recipient_zalo")
    private String recipientZalo;

    @Nationalized
    @Column(name = "message_type", nullable = false, length = 10)
    private String messageType;

    @Column(name = "message_text", columnDefinition = "TEXT")
    private String messageText;

    private String imageUrl;
    private String voiceUrl;
    private String musicUrl;

    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous = false;

    @Nationalized
    @ColumnDefault("'pending'")
    @Column(name = "approval_status", nullable = false, length = 10)
    private String approvalStatus;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Nationalized
    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "private_note", columnDefinition = "TEXT")
    private String privateNote;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

}