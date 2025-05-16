package com.enspd.movie_api.dto.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private String id;
    private String movieId;
    private String movieTitle;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 