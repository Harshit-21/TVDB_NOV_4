package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by harshit on 2/11/17.
 */

public class Bean_MovieImages {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("backdrops")
    @Expose
    private ArrayList<Bean_Poster> backdrop;
    @SerializedName("posters")
    @Expose
    private ArrayList<Bean_Poster> posters;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Bean_Poster> getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(ArrayList<Bean_Poster> backdrop) {
        this.backdrop = backdrop;
    }

    public ArrayList<Bean_Poster> getPosters() {
        return posters;
    }

    public void setPosters(ArrayList<Bean_Poster> posters) {
        this.posters = posters;
    }
}
