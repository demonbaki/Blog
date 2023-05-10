package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;

import it.cgmconsulting.myblog.payload.response.*;
import it.cgmconsulting.myblog.request.PostRequest;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {

    @Value("${app.post.image.path}")
    private String postImagePath;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public void save(Post p){
        postRepository.save(p);
    }

    public Post fromRequestToEntity(PostRequest postRequest, User u){
        return new Post(postRequest.getTitle(), postRequest.getContent(), postRequest.getOverview(), u);
    }

    public boolean existsByTitle(String title){
        return postRepository.existsByTitle(title);
    }

    public boolean existsByTitleAndIdNot(String title, long id){
        return postRepository.existsByTitleAndIdNot(title, id);
    }

    public List<PostBoxesResponse> getPostBoxes(){
        return postRepository.getPostBoxes();
    }

    public Optional<Post> findById(long id){
        return postRepository.findById(id);
    }

    public PostDetailResponse getByIdAndPublishedTrue(long id){
        return postRepository.getByIdAndPublishedTrue(id);
    }

    public Optional<Post> findByIdAndPublishedTrue(long id){
        return postRepository.findByIdAndPublishedTrue(id);
    }

    public Set<String> getCategoriesByPostId(long id){
        return postRepository.getCategoriesByPostId(id);
    }

    public PostPaginationResponse getSearchResults(
            int pageNumber, // numero della pagina da cui partire
            int pageSize, // numero di lementi per pagina
            String direction, // direzione dell'ordinamento ASC o DESC
            String sortBy, // per quale colonna effettuare l'ordinamento
            String keyword
    ){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostSearchResponse> results = postRepository.getPostsByKeyword(pageable, "%"+keyword+"%");
        PostPaginationResponse ppr = new PostPaginationResponse();
        if(results.hasContent())
            ppr.setPosts(results.getContent());

        ppr.setGetTotalPages(results.getTotalPages());
        ppr.setGetTotalElements(results.getTotalElements());
        return ppr;
    }

    public PostPdfResponse getByIdToPdf(long id){
        return postRepository.getByIdToPdf(id);
    }

    /****************** CHECK SU IMMAGINE ****************/

    public boolean checkSize(MultipartFile file, long postImageSize){
        if(!file.isEmpty() && file.getSize() > postImageSize)
            return true;
        return false;
    }

    public BufferedImage fromMultipartFileToBufferedImage(MultipartFile file){
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(file.getInputStream());
            return bf;
        } catch (IOException e) {
            return null;
        }
    }

    public boolean checkDimension(BufferedImage bf, int height, int width){
        if(bf == null)
            return false;
        return bf.getWidth()==width && bf.getHeight()==height;
    }

    public boolean checkExtensions(MultipartFile file, String[] extensions){
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        for (String extPermit: extensions) {
            if (ext.equals(extPermit))
                return true;
        }
        return false;
    }

    public String uploadFile(MultipartFile file, long id, String oldFileName){
        String fileName = id+"_"+file.getOriginalFilename();
        try {
            if(oldFileName != null) {
                Files.delete(Paths.get(postImagePath + oldFileName));
            }
            Path path = Paths.get(postImagePath + fileName);
            Files.write(path, file.getBytes());
            return fileName;
        } catch (IOException e) {
            return null;
        }
    }


}
