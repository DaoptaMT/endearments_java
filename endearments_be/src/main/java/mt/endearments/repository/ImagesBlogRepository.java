package mt.endearments.repository;

import mt.endearments.model.ImagesBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class ImagesBlogRepository implements JpaRepository<ImagesBlog, Long> {
}
