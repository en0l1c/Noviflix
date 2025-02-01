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
    @SerializedName("releaseYear")
    private int releaseYear;
    @SerializedName("url") // image url
    private String imageUrl;

    public Movie(String id, String title, String director, String plot, int releaseYear, String imageUrl) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.plot = plot;
        this.releaseYear = releaseYear;
        this.imageUrl = imageUrl;
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

    public int getReleaseYear() { return releaseYear; }

    public String getImageUrl() { return imageUrl; }
}