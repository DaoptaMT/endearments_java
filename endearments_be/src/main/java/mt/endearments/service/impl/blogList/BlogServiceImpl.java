package mt.endearments.service.impl.blogList;

import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mt.endearments.dto.response.blogList.BlogResponse;

import mt.endearments.dto.response.blogList.CategoryDto;
import mt.endearments.dto.response.blogList.ImagesBlogDto;
import mt.endearments.dto.response.blogList.UserDto;
import mt.endearments.model.Blog;
import mt.endearments.model.Category;
import mt.endearments.model.CategoryBlog;
import mt.endearments.repository.blogs.blogListRepo.BlogRepository;
import mt.endearments.service.blogList.BlogService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogServiceImpl implements BlogService {

    BlogRepository blogRepository;

    @Override
    public BlogResponse mapToBlogResponse(Blog blog) {
        List<CategoryDto> categoryDtos = (blog.getCategories() != null && !blog.getCategories().isEmpty())
                ? blog.getCategories().stream()
                .map(categoryBlog -> CategoryDto.builder()
                        .id(categoryBlog.getCategory().getId())
                        .name(categoryBlog.getCategory().getName())
                        .build())
                .toList()
                : List.of();

        List<ImagesBlogDto> imageDtos = (blog.getImages() != null && !blog.getImages().isEmpty())
                ? blog.getImages().stream()
                .map(imagesBlog -> ImagesBlogDto.builder()
                        .id(imagesBlog.getId())
                        .url(imagesBlog.getUrl())
                        .build())
                .toList()
                : List.of();

        Instant lastActive = (blog.getUpdatedAt() != null)
                ? blog.getUpdatedAt()
                : blog.getCreatedAt();

        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .categories(categoryDtos)
                .postedAt(lastActive)
                .author(UserDto.builder()
                        .id(blog.getAuthor().getId())
                        .name(blog.getAuthor().getName())
                        .build())
                .images(imageDtos)
                .build();
    }

    @Override
    public List<BlogResponse> getAllBlogs(String category, String sortField, String sortDirection) {
        Specification<Blog> spec = (root, query, cb) -> {
            if (query != null && sortField != null && !sortField.isEmpty()) {
                boolean desc = "desc".equalsIgnoreCase(sortDirection);

                if ("time".equals(sortField)) {
                    query.orderBy(desc
                            ? cb.desc(cb.coalesce(root.get("updatedAt"), root.get("createdAt")))
                            : cb.asc(cb.coalesce(root.get("updatedAt"), root.get("createdAt")))
                    );
                } else {
                    query.orderBy(desc
                            ? cb.desc(root.get(sortField))
                            : cb.asc(root.get(sortField))
                    );
                }
            }
            if (category != null && !category.isEmpty()) {
                Join<Blog, CategoryBlog> categoryBlogJoin = root.join("categories");
                Join<CategoryBlog, Category> categoryJoin = categoryBlogJoin.join("category");
                return cb.equal(categoryJoin.get("name"), category);
            }
            return null;
        };
        List<Blog> blogEntities = blogRepository.findAll(spec);

        return blogEntities.stream()
                .map(this::mapToBlogResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BlogResponse getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found with id: " + id));

        return mapToBlogResponse(blog);
    }
}
