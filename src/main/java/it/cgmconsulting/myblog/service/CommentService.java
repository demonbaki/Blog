package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save (Comment c){
        commentRepository.save(c);
    }
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    public List<CommentResponse>getCommentsByPost(long postId){
      return commentRepository.getCommentsByPost(postId);
    }

    public Optional<Comment> findByIdAndCensoredFalse(long id){
        return commentRepository.findByIdAndCensoredFalse(id);
    }

}
