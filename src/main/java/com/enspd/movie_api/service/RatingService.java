package com.enspd.movie_api.service;

import com.enspd.movie_api.dto.rating.RatingRequest;
import com.enspd.movie_api.dto.rating.RatingResponse;
import com.enspd.movie_api.model.Movie;
import com.enspd.movie_api.model.Rating;
import com.enspd.movie_api.model.User;
import com.enspd.movie_api.repository.MovieRepository;
import com.enspd.movie_api.repository.RatingRepository;
import com.enspd.movie_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Transactional
    public RatingResponse rateMovie(String movieId, RatingRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Rating rating = ratingRepository.findByMovieIdAndUserId(movieId, user.getId())
                .orElse(new Rating());

        rating.setMovie(movie);
        rating.setUser(user);
        rating.setScore(request.getScore());

        Rating savedRating = ratingRepository.save(rating);
        
        // Update movie's average rating
        Double averageRating = ratingRepository.getAverageRatingForMovie(movieId);
        movie.setAverageRating(averageRating);
        movieRepository.save(movie);

        return mapToResponse(savedRating);
    }

    public List<RatingResponse> getMovieRatings(String movieId) {
        return ratingRepository.findByMovieId(movieId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RatingResponse> getUserRatings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ratingRepository.findByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRating(String ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!rating.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own ratings");
        }

        ratingRepository.delete(rating);

        // Update movie's average rating
        Double averageRating = ratingRepository.getAverageRatingForMovie(rating.getMovie().getId());
        rating.getMovie().setAverageRating(averageRating);
        movieRepository.save(rating.getMovie());
    }

    private RatingResponse mapToResponse(Rating rating) {
        RatingResponse response = new RatingResponse();
        response.setId(rating.getId());
        response.setMovieId(rating.getMovie().getId());
        response.setMovieTitle(rating.getMovie().getTitle());
        response.setUsername(rating.getUser().getUsername());
        response.setScore(rating.getScore());
        response.setCreatedAt(rating.getCreatedAt());
        return response;
    }
} 