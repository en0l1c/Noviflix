package com.enolic.noviflix;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.enolic.noviflix.adapter.MovieAdapter;
import com.enolic.noviflix.api.ApiClient;
import com.enolic.noviflix.api.MovieApiService;
import com.enolic.noviflix.model.Movie;
import com.enolic.noviflix.tools.MovieUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class MainActivity extends AppCompatActivity {

//    private RecyclerView recyclerView; // to display the movies list
    private MovieAdapter adapter; // manages the appearance and the interactions of each movie
    private MovieApiService movieApiService;
    private final List<Movie> movies = new ArrayList<>();

    // because onActivityResult is deprecated. they are required for management returned data from other Activities
    private ActivityResultLauncher<Intent> addMovieLauncher;
    private ActivityResultLauncher<Intent> updateMovieLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView; // to display the movies list


        // initialize of ActivityResultLaunchers (it activates addMovieLauncher/updateMovieLauncher)
        setupActivityResultLaunchers();

        // BottomNavigationView setup
        setupBottomNavigationView();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //recyclerView use LinearLayout to show movie vertical
        // MovieAdapter is responsible for management of the appearance and handling(click, longclick) of each movie
        adapter = new MovieAdapter(
                movies,
                this::onMovieClicked,
                this::onMovieLongClicked
        );
        recyclerView.setAdapter(adapter);

        // SwipeRefreshLayout
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // fetchMovies from MovieUtils to reload movie list
            MovieUtils.fetchMovies(this, movieApiService, movies, adapter);

            // it stops the animation when the refresh is done
            swipeRefreshLayout.setRefreshing(false);
        });

        // Retrofit setup
        setupRetrofit();

        // it creates the movieApiService through Retrofit. also when connect to the server gets movies list
        setupRetrofit();

        // call fetchMovies from MovieUtils to get the movies and then refresh movie list (recyclerView)
        MovieUtils.fetchMovies(this, movieApiService, movies, adapter);
    }

    private void setupRetrofit() {
        movieApiService = ApiClient.getClient().create(MovieApiService.class);
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home); // set home as default
        // TODO: change it to switch-case
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_home) {
                // refresh movie list
                MovieUtils.fetchMovies(this, movieApiService, movies, adapter);
                return true;
            } else if (id == R.id.action_shuffle) {
                fetchRandomMovie();
                return true;
            } else if (id == R.id.action_add) {
                Intent intent = new Intent(MainActivity.this, AddUpdateMovieActivity.class);
                addMovieLauncher.launch(intent); // Use the launcher for adding a movie
                return true;
            } else {
                return false;
            }
        });
    }
    private void setupActivityResultLaunchers() {
        // initialization
        addMovieLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // refresh the movie list
                        MovieUtils.fetchMovies(this, movieApiService, movies, adapter);
                    }
                }
        );

        // initialization
        // it updates the movie list after an addition or update of a movie
        updateMovieLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String deletedMovieId = data.getStringExtra("DELETED_MOVIE_ID");
                        String movieId = data.getStringExtra("MOVIE_ID");

                        if (deletedMovieId != null) {
//                            // manage deleted movie
//                            movies.removeIf(movie -> movie.getId().equals(deletedMovieId));
//                            adapter.notifyDataSetChanged();

                            // create a list without the deleted movie
                            List<Movie> updatedMovies = new ArrayList<>(movies);
                            updatedMovies.removeIf(movie -> movie.getId().equals(deletedMovieId));

                            // update the adapter with DiffUtil
                            adapter.updateMovies(updatedMovies);
                        } else if (movieId != null) {
                            // manage updated movie
                            fetchMovieById(movieId);
                        }
                    }
                }
        );
    }

    private void fetchMovieById(String movieId) {
        MovieUtils.fetchMovieById(this, movieId, movieApiService, updatedMovie -> {
            boolean found = false;

            // Update the movie in the list if it exists
            for (int i = 0; i < movies.size(); i++) {
                if (movies.get(i).getId().equals(updatedMovie.getId())) {
                    movies.set(i, updatedMovie);
                    adapter.notifyItemChanged(i);
                    found = true;
                    break;
                }
            }

            // αdd the movie if it doesnt exist
            if (!found) {
                movies.add(updatedMovie);
                adapter.notifyItemInserted(movies.size() - 1); // refreshes only the new movie
            }
        });
    }

    private void fetchRandomMovie() {
        MovieUtils.fetchRandomMovie(this, movieApiService, randomMovie -> {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_DETAILS", randomMovie);
            startActivity(intent); // Display the movie in MovieDetailsActivity
        });
    }

    // When user clicks to a movie, he will be redirected to MovieDetailsActivity
    private void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("MOVIE_DETAILS", movie);
        updateMovieLauncher.launch(intent); // Use the launcher for updating/deleting a movie
    }

    // show actions for each movie if user pressed long on a movie_item
    private void onMovieLongClicked(Movie movie) {
        MovieUtils.showMovieActions(this, movie, movieApiService, () -> {
//            movies.remove(movie);
//            adapter.notifyDataSetChanged();
            // use fetchMovies to load the list from the backend
            MovieUtils.fetchMovies(this, movieApiService, movies, adapter);

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause: Activity is paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh the movie list
        MovieUtils.fetchMovies(this, movieApiService, movies, adapter); // Refresh movies
        Log.d("MainActivity", "onResume: Activity resumed, refreshing data");

        // select the right action button
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}
