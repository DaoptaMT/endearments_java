package mt.endearments.repository;

import mt.endearments.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BlogRepository implements JpaRepository<Blog, Long> {
}
