package com.enspd.movie_api.repository;

import com.enspd.movie_api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByMovieIdOrderByCreatedAtDesc(String movieId);
    List<Comment> findByUserIdOrderByCreatedAtDesc(String userId);
} 