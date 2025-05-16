package com.enspd.movie_api.controller;

import com.enspd.movie_api.dto.rating.RatingRequest;
import com.enspd.movie_api.dto.rating.RatingResponse;
import com.enspd.movie_api.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/movies/{movieId}")
    public ResponseEntity<RatingResponse> rateMovie(
            @PathVariable String movieId,
            @Valid @RequestBody RatingRequest request) {
        return ResponseEntity.ok(ratingService.rateMovie(movieId, request));
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<List<RatingResponse>> getMovieRatings(@PathVariable String movieId) {
        return ResponseEntity.ok(ratingService.getMovieRatings(movieId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<RatingResponse>> getUserRatings() {
        return ResponseEntity.ok(ratingService.getUserRatings());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable String id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }
} 