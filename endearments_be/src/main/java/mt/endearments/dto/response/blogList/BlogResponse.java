package mt.endearments.dto.response.blogList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private Instant postedAt;
    private UserDto author;
    private List<CategoryDto> categories;
    private List<ImagesBlogDto> images;
}
