package com.enolic.noviflix;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.enolic.noviflix.api.ApiClient;
import com.enolic.noviflix.api.MovieApiService;
import com.enolic.noviflix.model.Movie;
import com.enolic.noviflix.tools.MovieUtils;

public class AddUpdateMovieActivity extends AppCompatActivity {

    private MovieApiService movieApiService;
    private EditText titleInput, directorInput, plotInput, releaseYearInput, imageUrlInput;
    private Button actionButton;
    private Movie movieToEdit; // if its not declared globally the activity will be broken, TODO: make it local

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_movie);

        setupRetrofit();

        titleInput = findViewById(R.id.movie_title_input);
        directorInput = findViewById(R.id.movie_director_input);
        plotInput = findViewById(R.id.movie_plot_input);
        releaseYearInput = findViewById(R.id.movie_releaseYear_input);
        imageUrlInput = findViewById(R.id.movie_imageUrl_input);
        actionButton = findViewById(R.id.submit_movie_button);

        // for remaining character:
        TextView titleRemaining = findViewById(R.id.movie_title_remaining);
        TextView directorRemaining = findViewById(R.id.movie_director_remaining);
        TextView plotRemaining = findViewById(R.id.movie_plot_remaining);
        TextView imageUrlRemaining = findViewById(R.id.movie_imageUrl_remaining);

        addCharacterCountWatcher(titleInput, titleRemaining, 255);
        addCharacterCountWatcher(directorInput, directorRemaining, 255);
        addCharacterCountWatcher(plotInput, plotRemaining, 255);
        addCharacterCountWatcher(imageUrlInput, imageUrlRemaining, 255);


        // check if the user is editing or adding a movie from getting the intent
        movieToEdit = (Movie) getIntent().getSerializableExtra("MOVIE_DETAILS");
        if (movieToEdit != null) {
            setupForEditingMovie(movieToEdit);
        } else {
            setupForNewMovie();
        }
    }

    private void setupRetrofit() {
        movieApiService = ApiClient.getClient().create(MovieApiService.class);
    }

    private void setupForNewMovie() {
        actionButton.setText(R.string.add_movie);
        actionButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String director = directorInput.getText().toString().trim();
            String plot = plotInput.getText().toString().trim();
            int releaseYear;
            try {
                releaseYear = Integer.parseInt(releaseYearInput.getText().toString().trim());
                if (releaseYear < 1880 || releaseYear > 2025) {
                    releaseYearInput.setError("Year must be between 1880 and 2025");
                    return;
                }
            } catch (NumberFormatException e) {
                releaseYearInput.setError("Please enter a valid year");
                return;
            }
            String imageUrl = imageUrlInput.getText().toString().trim();

            if (validateInput(title, director, plot, releaseYear)) {
                Movie newMovie = new Movie(null, title, director, plot, releaseYear, imageUrl);
                MovieUtils.addMovie(this, newMovie, movieApiService, () -> {
                    setResult(RESULT_OK);
                    finish();
                });
            }
        });
    }

    private void setupForEditingMovie(Movie movie) {
        titleInput.setText(movie.getTitle());
        directorInput.setText(movie.getDirector());
        plotInput.setText(movie.getPlot());
        releaseYearInput.setText(String.valueOf(movie.getReleaseYear()));
        imageUrlInput.setText(movie.getImageUrl());
        actionButton.setText(R.string.update_movie);

        actionButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String director = directorInput.getText().toString().trim();
            String plot = plotInput.getText().toString().trim();
            int releaseYear;
            try {
                releaseYear = Integer.parseInt(releaseYearInput.getText().toString().trim());
                if (releaseYear < 1880 || releaseYear > 2025) {
                    releaseYearInput.setError("Year must be between 1880 and 2025");
                    return;
                }
            } catch (NumberFormatException e) {
                releaseYearInput.setError("Please enter a valid year");
                return;
            }
            String imageUrl = imageUrlInput.getText().toString().trim();

            if (validateInput(title, director, plot, releaseYear)) {
                Movie updatedMovie = new Movie(movie.getId(), title, director, plot, releaseYear, imageUrl);
                MovieUtils.updateMovie(this, updatedMovie, movieApiService, () -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("UPDATED_MOVIE", updatedMovie); // it returns the updated movie
                    setResult(RESULT_OK, resultIntent);
                    finish();
                });
            }
        });
    }

    private boolean validateInput(String title, String director, String plot, int releaseYear) {
        if (title.isEmpty() || director.isEmpty() || plot.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (releaseYear <= 1880 || releaseYear >= 2025) {
            Toast.makeText(this, "Not valid year!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addCharacterCountWatcher(EditText editText, TextView textView, int maxChars) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            // while text is changing
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remaining = maxChars - s.length();

                // remaining to String
                String remainingText = editText.getContext().getString(R.string.characters_remaining, remaining);

                textView.setText(remainingText);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
