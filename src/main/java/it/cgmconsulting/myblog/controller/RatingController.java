package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.PostService;
import it.cgmconsulting.myblog.service.RatingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("rating") // http:localhost:8080/rating/...
@SecurityRequirement(name = "myBlogSecurityScheme")
@Validated
public class RatingController {

    private final RatingService ratingService;
    private final PostService postService;
    public RatingController(RatingService ratingService, PostService postService) {
        this.ratingService = ratingService;
        this.postService = postService;
    }

    @PutMapping("/{id}/{rate}")
    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_READER')")
    public ResponseEntity<?> rate(@PathVariable long id, @PathVariable @Min(1) @Max(5) byte rate, @AuthenticationPrincipal UserPrincipal userPrincipal){

        Optional<Post> p = postService.findByIdAndPublishedTrue(id);
        if(!p.isPresent())
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);

        if(p.get().getAuthor().getId() == userPrincipal.getId())
            return new ResponseEntity("You cannot rate your own post", HttpStatus.FORBIDDEN);

        RatingId rId = new RatingId(new User(userPrincipal.getId()), new Post(p.get().getId()));
        Rating r = new Rating(rId, rate);
        ratingService.save(r);
        return new ResponseEntity("Your rate has benn registered", HttpStatus.OK);
    }
}
