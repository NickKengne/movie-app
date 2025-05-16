package com.enspd.movie_api.service;

import com.enspd.movie_api.dto.comment.CommentRequest;
import com.enspd.movie_api.dto.comment.CommentResponse;
import com.enspd.movie_api.model.Comment;
import com.enspd.movie_api.model.Movie;
import com.enspd.movie_api.model.User;
import com.enspd.movie_api.repository.CommentRepository;
import com.enspd.movie_api.repository.MovieRepository;
import com.enspd.movie_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse addComment(String movieId, CommentRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Comment comment = new Comment();
        comment.setMovie(movie);
        comment.setUser(user);
        comment.setContent(request.getContent());

        return mapToResponse(commentRepository.save(comment));
    }

    public List<CommentResponse> getMovieComments(String movieId) {
        return commentRepository.findByMovieIdOrderByCreatedAtDesc(movieId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getUserComments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return commentRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(String commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only update your own comments");
        }

        comment.setContent(request.getContent());
        return mapToResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setMovieId(comment.getMovie().getId());
        response.setMovieTitle(comment.getMovie().getTitle());
        response.setUsername(comment.getUser().getUsername());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }
} 