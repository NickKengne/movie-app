package com.enspd.movie_api.service;

import com.enspd.movie_api.dto.movie.MovieRequest;
import com.enspd.movie_api.dto.movie.MovieResponse;
import com.enspd.movie_api.model.Movie;
import com.enspd.movie_api.model.User;
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
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public MovieResponse createMovie(MovieRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDirector(request.getDirector());
        movie.setGenre(request.getGenre());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setCoverImage(request.getCoverImage());
        movie.setTrailerVideo(request.getTrailerVideo());
        movie.setCreatedBy(user);

        Movie savedMovie = movieRepository.save(movie);
        
        // Notify all users about the new movie
        notificationService.notifyNewMovie(savedMovie);

        return mapToResponse(savedMovie);
    }

    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MovieResponse getMovieById(String id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToResponse(movie);
    }

    @Transactional
    public MovieResponse updateMovie(String id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDirector(request.getDirector());
        movie.setGenre(request.getGenre());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setCoverImage(request.getCoverImage());
        movie.setTrailerVideo(request.getTrailerVideo());

        return mapToResponse(movieRepository.save(movie));
    }

    @Transactional
    public void deleteMovie(String id) {
        movieRepository.deleteById(id);
    }

    public List<MovieResponse> searchMovies(String query) {
        return movieRepository.findByTitleContainingIgnoreCase(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MovieResponse> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreIgnoreCase(genre).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MovieResponse> getTopRatedMovies() {
        return movieRepository.findTopRatedMovies().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MovieResponse mapToResponse(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescription(movie.getDescription());
        response.setDirector(movie.getDirector());
        response.setGenre(movie.getGenre());
        response.setReleaseYear(movie.getReleaseYear());
        response.setAverageRating(movie.getAverageRating());
        response.setCoverImage(movie.getCoverImage());
        response.setTrailerVideo(movie.getTrailerVideo());
        response.setCreatedBy(movie.getCreatedBy().getUsername());
        response.setCreatedAt(movie.getCreatedAt());
        response.setUpdatedAt(movie.getUpdatedAt());
        return response;
    }
} 