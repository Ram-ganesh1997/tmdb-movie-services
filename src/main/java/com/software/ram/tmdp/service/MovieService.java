package com.software.ram.tmdp.service;

import com.software.ram.tmdp.exception.InvalidDataException;
import com.software.ram.tmdp.exception.NotFoundException;
import com.software.ram.tmdp.model.Movie;
import com.software.ram.tmdp.repo.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    //CRUD : Create, Read, Update, Delete

    public Movie createMovie(Movie movie) {
        if (movie == null) {
            throw new InvalidDataException("Invalid movie: null");
        }
        return movieRepository.save(movie);
    }

    public Movie readMovie(Long id) {
       return movieRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Movie not found with id " + id));
    }

    public List<Movie> getMoviesList() {
        List<Movie> allMovies = movieRepository.findAll();
        return allMovies;
    }

    public void updateMovie(Long id, Movie updatedMovie) {
        if (updatedMovie == null || id == null) {
            throw new InvalidDataException("Invalid movie: null");
        }

        if(movieRepository.existsById(id)){
            Movie existingMovie = movieRepository.getReferenceById(id);
            existingMovie.setName(updatedMovie.getName());
            existingMovie.setDirector(updatedMovie.getDirector());
            existingMovie.setActors(updatedMovie.getActors());
            movieRepository.save(updatedMovie);
        }else{
            throw new NotFoundException("Movie not found with id " + id);
        }
    }

    public void deleteMovie(Long id) {
        if(movieRepository.existsById(id)){
            movieRepository.deleteById(id);
        }else {
            throw new NotFoundException("Movie not found with id " + id);
        }
    }


}
