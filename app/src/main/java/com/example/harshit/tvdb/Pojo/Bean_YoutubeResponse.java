package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by harshit on 2/11/17.
 */

public class Bean_YoutubeResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private ArrayList<Bean_Video> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Bean_Video> getResults() {
        return results;
    }

    public void setResults(ArrayList<Bean_Video> results) {
        this.results = results;
    }

}
