package com.enolic.noviflix.api;

import com.enolic.noviflix.model.Movie;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// (Api Service) - an interface that included all the endpoints
public interface MovieApiService {
    // Get all movies
    @GET("movies")
    Call<List<Movie>> getMovies();

    // Add movie
    @POST("movies")
    Call<Movie> addMovie(@Body Movie movie);

    // Delete Movie
    @DELETE("movies/{id}")
    Call<Void> deleteMovie(@Path("id") String id);

    // Update
    @PUT("movies/{id}")
    Call<Void> updateMovie(@Path("id") String id, @Body Movie movie);

    // Pick a random movie
    @GET("movies/whatsnext")
    Call<Movie> getRandomMovie();

    // Get movie by id
    @GET("movies/{id}")
    Call<Movie> getMovieById(@Path("id") String id);

    // GET or POST? for  posting hardcoded movies??
//    @GET("movies/loadmovies")
//    Call<Void> loadMovies();


}