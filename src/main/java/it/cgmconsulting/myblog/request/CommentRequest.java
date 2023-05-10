package it.cgmconsulting.myblog.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequest {

    @Min(1)
    private long postId;


    @NotBlank @Size(max = 255)
    private String comment;
}
