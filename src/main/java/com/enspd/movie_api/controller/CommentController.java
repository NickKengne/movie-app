package com.enspd.movie_api.controller;

import com.enspd.movie_api.dto.comment.CommentRequest;
import com.enspd.movie_api.dto.comment.CommentResponse;
import com.enspd.movie_api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/movies/{movieId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable String movieId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(movieId, request));
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<List<CommentResponse>> getMovieComments(@PathVariable String movieId) {
        return ResponseEntity.ok(commentService.getMovieComments(movieId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<CommentResponse>> getUserComments() {
        return ResponseEntity.ok(commentService.getUserComments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable String id,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
} 