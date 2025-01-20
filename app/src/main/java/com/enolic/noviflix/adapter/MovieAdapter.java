package com.enolic.noviflix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.enolic.noviflix.R;
import com.enolic.noviflix.model.Movie;
import com.enolic.noviflix.tools.MovieDiffCallback;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movies;
    private final OnMovieClickListener onMovieClickListener;
    private final OnMovieLongClickListener onMovieLongClickListener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public interface OnMovieLongClickListener {
        void onMovieLongClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movies, OnMovieClickListener clickListener, OnMovieLongClickListener longClickListener) {
        this.movies = movies;
        this.onMovieClickListener = clickListener;
        this.onMovieLongClickListener = longClickListener;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView directorTextView;
        public TextView plotTextView;

        // itemView is the root of the LinearLayout and other views are inside LinearLayout
        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movie_title);
            directorTextView = itemView.findViewById(R.id.movie_director);
            plotTextView = itemView.findViewById(R.id.movie_plot);
        }
    }

    // update data with DiffUtil
    public void updateMovies(List<Movie> newMovies) {
        // calculate diff with MovieDiffCallback
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MovieDiffCallback(movies, newMovies));

        // update the list with new data
        movies.clear();
        movies.addAll(newMovies);

        // apply changes to RecyclerView
        diffResult.dispatchUpdatesTo(this);
    }




    @Override
    @NonNull
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    // it's called only for the movies that can fit in the screen.
    // if screen can fit 3 movies, the method called for pos=0, pos=1, pos=2
    // after scroll to the fourth movie, the positions for the movies before
    // remain the same, and after the onBindViewHolder() start for pos=3,pos=4,...
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.titleTextView.setText(movie.getTitle());
        holder.directorTextView.setText(
                holder.itemView.getContext().getString(R.string.director, movie.getDirector())
        );
        holder.plotTextView.setText(
                holder.itemView.getContext().getString(R.string.plot, movie.getPlot())
        );

        holder.itemView.setOnClickListener(v -> onMovieClickListener.onMovieClick(movie));

        holder.itemView.setOnLongClickListener(v -> {
            onMovieLongClickListener.onMovieLongClick(movie);
            return true;
        });
    }
    // counts the size of the movies list to let know the RecyclerView how many movies to display
    @Override
    public int getItemCount() {
        return movies.size();
    }
}
