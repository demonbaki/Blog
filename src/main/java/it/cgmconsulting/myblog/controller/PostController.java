package it.cgmconsulting.myblog.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;

import it.cgmconsulting.myblog.payload.response.PostDetailResponse;
import it.cgmconsulting.myblog.payload.response.PostPaginationResponse;
import it.cgmconsulting.myblog.request.CategoryRequest;
import it.cgmconsulting.myblog.request.PostRequest;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.CategoryService;
import it.cgmconsulting.myblog.service.CommentService;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("post") // http:localhost:8080/post/...
@SecurityRequirement(name = "myBlogSecurityScheme")
@Validated
public class PostController {

    @Value("${app.post.image.size}")
    private long postImageSize;

    @Value("${app.post.image.width}")
    private int postImageWidth;

    @Value("${app.post.image.height}")
    private int postImageHeight;

    @Value("${app.post.image.extensions}")
    private String[] postImageExtensions;


    private final PostService postService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    public PostController(PostService postService, CategoryService categoryService, CommentService commentService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.commentService = commentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_WRITER')")
    public ResponseEntity<?> createPost(@RequestBody @Valid PostRequest postRequest, @AuthenticationPrincipal UserPrincipal userPrincipal){
        // Il 'title' è UNIQUE sul db, quindi devo verificare che non esista già un post con il titolo che gli sto passando dalla request
        if(postService.existsByTitle(postRequest.getTitle()))
            return new ResponseEntity<>("Post title already present", HttpStatus.BAD_REQUEST);

        Post p = postService.fromRequestToEntity(postRequest, new User(userPrincipal.getId()));
        postService.save(p);
        return new ResponseEntity<>("Post created", HttpStatus.CREATED);
    }

    @GetMapping("/public/boxes")
    @Cacheable("RiquadriHomePage")
    public ResponseEntity<?> getBoxes(Logger log){
        log.info("Retriving boxes for Home Page");
        return new ResponseEntity<>(postService.getPostBoxes(), HttpStatus.OK);
    }

    @PutMapping(value="/publication-flow/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = "RiquadriHomePage", allEntries = true)
    @Transactional
    public ResponseEntity<?> publicationFlow(@PathVariable long id){

        Optional<Post> p = postService.findById(id);
        if(!p.isPresent())
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);
        p.get().setPublished(!p.get().isPublished());
        return new ResponseEntity<>("Post status has been updated", HttpStatus.OK);
    }

    @PatchMapping(value="/add-image/{id}", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_WRITER')")
    @Transactional
    public ResponseEntity<?> addImage(@PathVariable long id, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam MultipartFile file){

        if(postService.checkSize(file, postImageSize))
            return new ResponseEntity("File size is too large", HttpStatus.BAD_REQUEST);
        if(!postService.checkDimension(postService.fromMultipartFileToBufferedImage(file), postImageHeight, postImageWidth))
            return new ResponseEntity("Wrong file dimensions", HttpStatus.BAD_REQUEST);
        if(postService.checkExtensions(file, postImageExtensions))
            return new ResponseEntity("File extension not allowed", HttpStatus.BAD_REQUEST);

        Optional<Post> p = postService.findById(id);

        if(!p.isPresent())
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);
        try {
            if(userPrincipal.getId() != p.get().getAuthor().getId())
                return new ResponseEntity("You are not the author of this post", HttpStatus.FORBIDDEN);
            String fileName = postService.uploadFile(file, id, p.get().getImage());
            p.get().setImage(fileName);
        } catch (Exception e){
            return new ResponseEntity("Something went wrong: image not uploaded", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Image uploaded", HttpStatus.OK);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<?> getPostDetail(@PathVariable long id){
        PostDetailResponse p = postService.getByIdAndPublishedTrue(id);
        if(p != null) {
            p.setCategories(postService.getCategoriesByPostId(id));
            p.setComments(commentService.getCommentsByPost(id));
            return new ResponseEntity(p, HttpStatus.OK);
        }
        return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_WRITER')")
    @CacheEvict(value = "RiquadriHomePage", allEntries = true)
    @Transactional
    public ResponseEntity<?> updatePost(@PathVariable long id, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostRequest request){
        Optional<Post> post = postService.findById(id);

        if(post.isEmpty()){
            return new ResponseEntity<>("Post not found",HttpStatus.NOT_FOUND);
        }

        if(post.get().getAuthor().getId() != userPrincipal.getId()){
            return new ResponseEntity<>("Only Author can modify his post",HttpStatus.BAD_REQUEST);
        }

        if(postService.existsByTitleAndIdNot(request.getTitle(), id)){
            return new ResponseEntity<>("Title already present",HttpStatus.BAD_REQUEST);
        }

        post.get().setPublished(false);
        post.get().setTitle(request.getTitle());
        post.get().setContent(request.getContent());
        post.get().setOverview(request.getOverview());

        return new ResponseEntity<>("Post updated",HttpStatus.OK);
    }

    /******************************** ASSOCIAZIONE CATEGORIE ******************************************/

    @PutMapping("/add-categories/{id}")
    @PreAuthorize("hasRole('ROLE_WRITER')")
    @Transactional
    public ResponseEntity<?> addCategories(@PathVariable long id, @RequestBody Set<CategoryRequest> categories){

        Optional<Post> post = postService.findById(id);
        if(post.isEmpty()){
            return new ResponseEntity<>("Post not found",HttpStatus.NOT_FOUND);
        }

        Set<Category> cats = categoryService.findByCategoryNameInAndVisibleTrue(categories.stream().map(c -> c.getCategoryName()).collect(Collectors.toSet()));
        post.get().setCategories(cats);

        if(cats.isEmpty())
            return new ResponseEntity<>("Removed all categories from post",HttpStatus.OK);

        return new ResponseEntity<>("Categories added to post",HttpStatus.OK);
    }

    // PAGINAZIONE DEI RISULTATI
    // ricerca sui titoli dei post per parola chiave
    @GetMapping("/public/search/{keyword}")
    public ResponseEntity<?> search(
            @PathVariable @NotBlank String keyword,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "title") String sortBy
    ){

        PostPaginationResponse ppr = postService.getSearchResults(pageNumber, pageSize, direction, sortBy, keyword);
        return new ResponseEntity(ppr, HttpStatus.OK);
    }



}

