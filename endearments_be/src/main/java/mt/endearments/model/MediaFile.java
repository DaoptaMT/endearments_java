package mt.endearments.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "media_files", schema = "dbo")
public class MediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "message_id", nullable = false)
    private mt.endearments.model.Message message;

    @Nationalized
    @Column(name = "file_type", nullable = false, length = 10)
    private String fileType;

    @Nationalized
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Nationalized
    @Column(name = "file_name")
    private String fileName;

    @Nationalized
    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "\"size\"")
    private Long size;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}