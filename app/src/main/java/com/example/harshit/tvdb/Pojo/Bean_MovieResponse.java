package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Harshit on 9/16/2017.
 */

public class Bean_MovieResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<Bean_MovieDetails> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Bean_MovieDetails> getResults() {
        return results;
    }

    public void setResults(ArrayList<Bean_MovieDetails> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
