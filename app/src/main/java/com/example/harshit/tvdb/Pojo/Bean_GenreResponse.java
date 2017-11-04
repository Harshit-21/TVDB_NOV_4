package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Harshit on 10/24/2017.
 */

public class Bean_GenreResponse {
    @SerializedName("genres")
    @Expose
    private ArrayList<Bean_Genre> genres = null;

    public ArrayList<Bean_Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Bean_Genre> genres) {
        this.genres = genres;
    }
}
