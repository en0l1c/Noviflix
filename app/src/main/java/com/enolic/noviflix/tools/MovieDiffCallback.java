package com.enolic.noviflix.tools;

import androidx.recyclerview.widget.DiffUtil;
import com.enolic.noviflix.model.Movie;

import java.util.List;

public class MovieDiffCallback extends DiffUtil.Callback {
    private final List<Movie> oldList;
    private final List<Movie> newList;

    public MovieDiffCallback(List<Movie> oldList, List<Movie> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // συγκριση με βαση το id
        return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // συγκριση του περιεχομενου
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
