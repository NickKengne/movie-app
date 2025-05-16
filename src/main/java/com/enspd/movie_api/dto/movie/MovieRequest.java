package com.enspd.movie_api.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MovieRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 255, message = "Director name must not exceed 255 characters")
    private String director;

    @Size(max = 100, message = "Genre must not exceed 100 characters")
    private String genre;

    @NotNull(message = "Release year is required")
    private Integer releaseYear;

    @Size(max = 255, message = "Cover image URL must not exceed 255 characters")
    private String coverImage;

    @Size(max = 255, message = "Trailer video URL must not exceed 255 characters")
    private String trailerVideo;
} 