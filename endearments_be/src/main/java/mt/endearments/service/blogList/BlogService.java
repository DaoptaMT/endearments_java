package mt.endearments.service.blogList;

import mt.endearments.dto.response.blogList.BlogResponse;
import mt.endearments.model.Blog;
import mt.endearments.repository.blogs.blogListRepo.BlogRepository;

import java.util.List;

public interface BlogService {

    BlogResponse mapToBlogResponse(Blog blog);

    List<BlogResponse> getAllBlogs(String category, String sortField, String sortDirection);

    BlogResponse getBlogById(Long id);
}
