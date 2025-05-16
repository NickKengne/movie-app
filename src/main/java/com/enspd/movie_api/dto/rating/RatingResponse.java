package com.enspd.movie_api.dto.rating;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingResponse {
    private String id;
    private String movieId;
    private String movieTitle;
    private String username;
    private Integer score;
    private LocalDateTime createdAt;
} 