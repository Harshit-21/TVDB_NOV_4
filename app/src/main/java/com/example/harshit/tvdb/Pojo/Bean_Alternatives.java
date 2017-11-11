package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Harshit on 11/12/2017.
 */

public class Bean_Alternatives {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("titles")
    @Expose
    private ArrayList<Bean_Translations> titles = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Bean_Translations> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<Bean_Translations> titles) {
        this.titles = titles;
    }
}
