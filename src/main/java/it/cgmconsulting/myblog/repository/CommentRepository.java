package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.CommentResponse(" +
            "c.id, " +
            "CASE WHEN (c.censored=true) THEN '***********' ELSE c.comment END, " +
           // "c.comment, " +
            "c.author.username, " +
            "c.createdAt) " +
            "FROM Comment c " +
            "WHERE c.post.id = :id and c.post.published=true " +
            "ORDER BY c.createdAt DESC")
    List<CommentResponse> getCommentsByPost(@Param("id") long postId);


    Optional<Comment> findByIdAndCensoredFalse(long id);
}
