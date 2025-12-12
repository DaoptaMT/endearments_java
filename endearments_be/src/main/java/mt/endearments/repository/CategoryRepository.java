package mt.endearments.repository;

import mt.endearments.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class CategoryRepository implements JpaRepository<Category, Long> {
}
