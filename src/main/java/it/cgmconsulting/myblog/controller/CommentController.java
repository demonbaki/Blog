package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.payload.response.PostDetailResponse;
import it.cgmconsulting.myblog.request.CommentRequest;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.CategoryService;
import it.cgmconsulting.myblog.service.CommentService;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("comment") // http:localhost:8080/post/...
@SecurityRequirement(name = "myBlogSecurityScheme")
@Validated
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final CategoryService categoryService;

    public CommentController(CommentService commentService, PostService postService, CategoryService categoryService) {
        this.commentService = commentService;
        this.postService = postService;
        this.categoryService = categoryService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_READER')")
    public ResponseEntity<?> save(@RequestBody @Valid CommentRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal){
        Optional<Post> p = postService.findByIdAndPublishedTrue(request.getPostId());
        if (!p.isPresent())
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        Comment c =  new Comment (request.getComment(), new User(userPrincipal.getId()), p.get());
        commentService.save(c);
        return new ResponseEntity<>("New comment added", HttpStatus.CREATED);
    }







}
