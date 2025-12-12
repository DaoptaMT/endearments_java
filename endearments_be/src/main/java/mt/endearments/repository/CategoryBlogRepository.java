package mt.endearments.repository;

import mt.endearments.model.CategoryBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class CategoryBlogRepository implements JpaRepository<CategoryBlog, Long> {
}
