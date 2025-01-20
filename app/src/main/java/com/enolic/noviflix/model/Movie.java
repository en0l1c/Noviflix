package com.enolic.noviflix.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// Movie class implementing Serializable, to be able to move data as serial to other Activities.
public class Movie implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("director")
    private String director; // Added director
    @SerializedName("plot")
    private String plot; // Added plot
    //You can add genre and year if your api has them

    public Movie(String id, String title, String director, String plot) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.plot = plot;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getPlot() {
        return plot;
    }
}