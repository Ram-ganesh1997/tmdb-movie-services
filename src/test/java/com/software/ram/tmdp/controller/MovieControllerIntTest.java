package com.software.ram.tmdp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.ram.tmdp.model.Movie;
import com.software.ram.tmdp.repo.MovieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void cleanUp()
    {
        movieRepository.deleteAllInBatch();
    }

    @Test
    void givenMovie_whenCreateMovie_thenReturnMovie() throws Exception {

        //Given
        Movie movie = new Movie();
        movie.setName("RRR");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("Jr Ntr","Ram Charan","Alia Bhatt"));

        //Create
        var response = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        //Then verify saved movie
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.name", Matchers.is(movie.getName())))
                .andExpect(jsonPath("$.director", Matchers.is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", Matchers.is(movie.getActors())));
        }
    @Test
    void givenMovieId_whenFetchMovie_thenReturnMovie() throws Exception {

            //Given
            Movie movie = new Movie();
            movie.setName("RRR");
            movie.setDirector("SS Rajamouli");
            movie.setActors(List.of("Jr Ntr","Ram Charan","Alia Bhatt"));

        Movie savedMovie = movieRepository.save(movie);

        //When
        var response = mockMvc.perform(get("/movies/" + savedMovie.getId()));

        //Then verify saved movie
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name", Matchers.is(movie.getName())))
                .andExpect(jsonPath("$.director", Matchers.is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", Matchers.is(movie.getActors())));
    }

    @Test
    void givenMovieId_whenUpdateMovie_thenReturnUpdatedMovie() throws Exception {

        //Given
        Movie movie = new Movie();
        movie.setName("RRR");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("Jr Ntr","Ram Charan","Alia Bhatt"));

        Movie savedMovie = movieRepository.save(movie);
       Long id = savedMovie.getId();

       //update movie
        movie.setActors(List.of("Jr Ntr","Ram Charan","Alia Bhatt","Ajay Devgan"));

        var response = mockMvc.perform(put("/movies/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        //Then verify updated movie
        response.andDo(print())
                .andExpect(status().isOk());

        var fetchResponse =   mockMvc.perform(get("/movies/" + savedMovie.getId()));

        fetchResponse.andDo(print())
                .andExpect(jsonPath("$.id", Matchers.is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name", Matchers.is(movie.getName())))
                .andExpect(jsonPath("$.director", Matchers.is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", Matchers.is(movie.getActors())));
    }

    @Test
    void givenMovieId_whenDeleteMovie_thenReturnDeletedMovie() throws Exception {

        //Given
        Movie movie = new Movie();
        movie.setName("RRR");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("Ram Charan","Alia Bhatt"));
        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        //When
        var response = mockMvc.perform(delete("/movies/" + id)
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));
        //Then
        response.andDo(print())
                .andExpect(status().isOk());

        //Verfiy
        assertFalse(movieRepository.findById(id).isPresent());
    }

}