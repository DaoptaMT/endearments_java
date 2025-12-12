package mt.endearments.repository;

import mt.endearments.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class MediaFileRepository implements JpaRepository<MediaFile, Long> {
}
