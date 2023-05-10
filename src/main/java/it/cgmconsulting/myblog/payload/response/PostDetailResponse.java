package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PostDetailResponse {

    private long id;
    private String title;
    private String overview;
    private String image;
    private String avg;
    Set<String> categories;
    List<CommentResponse> comments;

    public PostDetailResponse(long id, String title, String overview, String image, String avg) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.image = image;
        this.avg = avg;
    }
}

