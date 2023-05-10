package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostPdfResponse {

    private long id;
    private String title;
    private String content;
    private String image;
    private String username;
    private LocalDateTime updatedAt;

}
