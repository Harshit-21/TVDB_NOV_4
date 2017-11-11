package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Harshit on 11/11/2017.
 */

public class Bean_ReleaseDateResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private ArrayList<Bean_Release>results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Bean_Release> getResults() {
        return results;
    }

    public void setResults(ArrayList<Bean_Release> results) {
        this.results = results;
    }
}
