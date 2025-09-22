package com.software.ram.tmdp.controller;

import com.software.ram.tmdp.model.Movie;
import com.software.ram.tmdp.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Slf4j
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovies(@PathVariable Long id){

        Movie movie = movieService.readMovie(id);
        log.info("Returning movie with id {}", id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Movie>> getMoviesList(){
        List<Movie> movie = movieService.getMoviesList();
        log.info("Returning movies list ");
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie){
        Movie movie1 = movieService.createMovie(movie);
        log.info("Creating movie with id {}", movie.getId());
        return new ResponseEntity<>(movie1, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public void updateMovie(@PathVariable Long id, @RequestBody Movie movie){
        movieService.updateMovie(id, movie);
        log.info("Updating movie with id {}", id);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
        log.info("Deleting movie with id {}", id);
    }
}
