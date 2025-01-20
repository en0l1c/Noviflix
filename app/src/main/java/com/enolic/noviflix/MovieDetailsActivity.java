package com.enolic.noviflix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.enolic.noviflix.api.ApiClient;
import com.enolic.noviflix.api.MovieApiService;
import com.enolic.noviflix.model.Movie;
import com.enolic.noviflix.tools.MovieUtils;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieApiService movieApiService;
    private Movie movie;
    private ActivityResultLauncher<Intent> editMovieLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        setupRetrofit();
        setupActivityResultLauncher();

        String movieId = getIntent().getStringExtra("MOVIE_ID");
        if (movieId != null) {
            fetchMovieById(movieId);
        } else {
            movie = (Movie) getIntent().getSerializableExtra("MOVIE_DETAILS");
            if (movie != null) {
                updateUI(movie);
            }
        }

        Button deleteButton = findViewById(R.id.delete_movie_button);
        Button editButton = findViewById(R.id.edit_movie_button);

        deleteButton.setOnClickListener(v -> deleteMovie(movie.getId()));
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(MovieDetailsActivity.this, AddUpdateMovieActivity.class);
            editIntent.putExtra("MOVIE_DETAILS", movie);
            editMovieLauncher.launch(editIntent); // launch AddUpdateMovieActivity when "edit" clicked
        });
    }

    private void setupRetrofit() {
        movieApiService = ApiClient.getClient().create(MovieApiService.class);
    }

    private void setupActivityResultLauncher() {
        editMovieLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Movie updatedMovie = (Movie) result.getData().getSerializableExtra("UPDATED_MOVIE");
                        if (updatedMovie != null) {
                            movie = updatedMovie;
                            updateUI(movie);

                            // return the updated movie to the MainActivity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("UPDATED_MOVIE", updatedMovie);
                            resultIntent.putExtra("MOVIE_ID", updatedMovie.getId());
                            setResult(RESULT_OK, resultIntent);
                        }
                    }
                }
        );
    }

    private void fetchMovieById(String movieId) {
        MovieUtils.fetchMovieById(this, movieId, movieApiService, fetchedMovie -> {
            movie = new Movie(
                    fetchedMovie.getId(),
                    fetchedMovie.getTitle() != null ? fetchedMovie.getTitle() : "No title available",
                    fetchedMovie.getDirector() != null ? fetchedMovie.getDirector() : "Unknown director",
                    fetchedMovie.getPlot() != null ? fetchedMovie.getPlot() : "No plot available"
            );
            updateUI(movie); // update the ui to use the latest new movie data
        });
    }

    private void updateUI(Movie movie) {
        TextView titleTextView = findViewById(R.id.movie_details_title);
        TextView directorTextView = findViewById(R.id.movie_details_director);
        TextView plotTextView = findViewById(R.id.movie_details_plot);

        // verify data before display them
        String title = movie.getTitle() != null ? movie.getTitle() : "No title available";
        String director = movie.getDirector() != null ? movie.getDirector() : "Unknown director";
        String plot = movie.getPlot() != null ? movie.getPlot() : "No plot available";

        titleTextView.setText(title);
        directorTextView.setText(getString((R.string.director), director));
        plotTextView.setText(plot);
    }

    private void deleteMovie(String movieId) {
        MovieUtils.deleteMovie(this, movieId, movieApiService, () -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("DELETED_MOVIE_ID", movieId);
            setResult(RESULT_OK, resultIntent);
            finish(); // exit activity after deleting the movie
        });
    }
}
