package com.enolic.noviflix.tools;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import com.enolic.noviflix.AddUpdateMovieActivity;
import com.enolic.noviflix.R;
import com.enolic.noviflix.adapter.MovieAdapter;
import com.enolic.noviflix.api.MovieApiService;
import com.enolic.noviflix.model.Movie;
import java.util.List;
import java.util.function.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieUtils {

    public static void fetchMovieById(Context context, String movieId, MovieApiService movieApiService, Consumer<Movie> onSuccess) {
        Call<Movie> call = movieApiService.getMovieById(movieId);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // callback.accept(response.body());
                    onSuccess.accept(response.body()); // this is the fetchedMovie (it returns the movie/fetchedmovie throught Consumer)
                } else {
                    ErrorHandler.handleError(response, context, "MovieUtils -> fetchMovieById");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                ErrorHandler.handleFailure(t, context, "MovieUtils -> fetchMovieById");
            }
        });
    }

    public static void fetchMovies(Context context, MovieApiService movieApiService, List<Movie> movieList, MovieAdapter adapter) {
        Call<List<Movie>> call = movieApiService.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> newMovies = response.body();

                    // update the list with DiffUtil for best results
                    MovieDiffCallback diffCallback = new MovieDiffCallback(movieList, newMovies);
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

                    // update the list
                    movieList.clear();
                    movieList.addAll(newMovies);

                    // if the list is eempty or it changed call notifyDataSetChanged
                    if (movieList.isEmpty()) {
                        adapter.notifyDataSetChanged();
                    } else {
                        diffResult.dispatchUpdatesTo(adapter);
                    }
                } else {
                    ErrorHandler.handleError(response, context, "MovieUtils -> fetchMovies");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                ErrorHandler.handleFailure(t, context, "MovieUtils -> fetchMovies");
            }
        });
    }


    public static void fetchRandomMovie(Context context, MovieApiService movieApiService, Consumer<Movie> onSuccess) {
        Call<Movie> call = movieApiService.getRandomMovie();
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                } else {
                    ErrorHandler.handleError(response, context, "MovieUtils -> fetchRandomMovie");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                ErrorHandler.handleFailure(t, context, "MovieUtils -> fetchRandomMovie");
            }
        });
    }




    public static void addMovie(Context context, Movie movie, MovieApiService movieApiService, Runnable onAddSuccess) {
        Call<Movie> call = movieApiService.addMovie(movie);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Movie added successfully!", Toast.LENGTH_SHORT).show();
                    if (onAddSuccess != null) {
                        onAddSuccess.run(); // update after the addition
                    }
                } else {
                    ErrorHandler.handleError(response, context, "MovieUtils -> addMovie");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                ErrorHandler.handleFailure(t, context, "MovieUtils -> addMovie");
            }
        });
    }

    // for showMovieActions() in MovieUtils
    public static void updateMovie(Context context, Movie movie) {
        Intent intent = new Intent(context, AddUpdateMovieActivity.class);
        intent.putExtra("MOVIE_DETAILS", movie); // movie for edit
        context.startActivity(intent);
    }

    // for AddUpdateMovieActivity
    public static void updateMovie(Context context, Movie movie, MovieApiService movieApiService, Runnable onUpdateSuccess) {
        Call<Void> call = movieApiService.updateMovie(movie.getId(), movie);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Movie updated successfully!", Toast.LENGTH_SHORT).show();
                    if (onUpdateSuccess != null) onUpdateSuccess.run();
                } else {
                    ErrorHandler.handleError(response, context, "MovieUtils -> updateMovie");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                ErrorHandler.handleFailure(t, context, "MovieUtils -> updateMovie");
            }
        });
    }


    public static void deleteMovie(Context context, String movieId, MovieApiService movieApiService, Runnable onDeleteSuccess) {
        Call<Void> call = movieApiService.deleteMovie(movieId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Movie deleted successfully!", Toast.LENGTH_SHORT).show();
                    if (onDeleteSuccess != null) {
                        onDeleteSuccess.run(); // callback after deletion
                    }
                } else {
                    ErrorHandler.handleError(response, context, "MovieUtils -> deleteMovie");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                ErrorHandler.handleFailure(t, context, "MovieUtils -> deleteMovie");
            }
        });
    }



    public static void showMovieActions(Context context, Movie movie, MovieApiService movieApiService, Runnable onDeleteSuccess) {
        String[] actions = new String[]{
                context.getString(R.string.edit_movie),
                context.getString(R.string.delete_movie)
        };

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.choose_action))
                .setItems(actions, (dialog, which) -> {
                    if (which == 0) {
                        updateMovie(context, movie); // EDIT movie
                    } else if (which == 1) {
                        deleteMovie(context, movie.getId(), movieApiService, onDeleteSuccess); // delete movie
                    }
                })
                .show();
    }
}
