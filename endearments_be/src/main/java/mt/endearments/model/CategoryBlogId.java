package mt.endearments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class CategoryBlogId implements Serializable {
    private static final long serialVersionUID = -3063151109251669444L;
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "blog_id", nullable = false)
    private Long blogId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CategoryBlogId entity = (CategoryBlogId) o;
        return Objects.equals(this.blogId, entity.blogId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blogId, categoryId);
    }

}