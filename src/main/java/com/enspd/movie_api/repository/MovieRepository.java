package com.enspd.movie_api.repository;

import com.enspd.movie_api.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, String> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByGenreIgnoreCase(String genre);
    List<Movie> findByReleaseYear(Integer releaseYear);
    
    @Query("SELECT m FROM Movie m ORDER BY m.averageRating DESC")
    List<Movie> findTopRatedMovies();
} 