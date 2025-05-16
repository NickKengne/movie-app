package com.enspd.movie_api.dto.movie;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieResponse {
    private String id;
    private String title;
    private String description;
    private String director;
    private String genre;
    private Integer releaseYear;
    private Double averageRating;
    private String coverImage;
    private String trailerVideo;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 