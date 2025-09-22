package com.software.ram.tmdp.service;

import com.software.ram.tmdp.model.Movie;
import com.software.ram.tmdp.repo.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    //CRUD : Create, Read, Update, Delete

    public Movie createMovie(Movie movie) {
        if (movie == null) {
            throw new RuntimeException("Invalid movie");
        }
        return movieRepository.save(movie);
    }

    public Movie readMovie(Long id) {
       return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public void updateMovie(Long id, Movie updatedMovie) {
        if (updatedMovie == null || id == null) {
            throw new IllegalArgumentException("Invalid movie data");
        }

        if(movieRepository.existsById(id)){
            Movie existingMovie = movieRepository.getReferenceById(id);
            existingMovie.setName(updatedMovie.getName());
            existingMovie.setDirector(updatedMovie.getDirector());
            existingMovie.setActors(updatedMovie.getActors());
            movieRepository.save(updatedMovie);
        }else{
            throw new RuntimeException("Movie not found");
        }
    }

    public void deleteMovie(Long id) {
        if(movieRepository.existsById(id)){
            movieRepository.deleteById(id);
        }else {
            throw new RuntimeException("Movie not found");
        }
    }


}
