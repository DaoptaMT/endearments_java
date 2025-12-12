package mt.endearments.repository.blogs.blogListRepo;

import mt.endearments.model.Blog;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @EntityGraph(attributePaths = {"author", "categories.category", "images"})
    List<Blog> findAll(Specification<Blog> spec, Sort sort);
}
