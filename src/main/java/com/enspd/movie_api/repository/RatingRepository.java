package com.enspd.movie_api.repository;

import com.enspd.movie_api.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, String> {
    List<Rating> findByMovieId(String movieId);
    List<Rating> findByUserId(String userId);
    Optional<Rating> findByMovieIdAndUserId(String movieId, String userId);
    
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.movie.id = ?1")
    Double getAverageRatingForMovie(String movieId);
} 