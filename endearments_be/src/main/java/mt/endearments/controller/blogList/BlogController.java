package mt.endearments.controller.blogList;

import mt.endearments.dto.response.blogList.BlogResponse;
import mt.endearments.service.blogList.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs/query")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlogs(
            @RequestParam(required = false, defaultValue = "time") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String category
    ) {
        List<BlogResponse> blogs = blogService.getAllBlogs(category, sortField, sortDirection);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getBlogDetail(@PathVariable Long id) {
        BlogResponse blog = blogService.getBlogById(id);
        return ResponseEntity.ok(blog);
    }
}
