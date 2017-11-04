package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by harshit on 3/11/17.
 */

public class Bean_CastnCrewResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private ArrayList<Bean_CastnCrew> cast;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Bean_CastnCrew> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Bean_CastnCrew> cast) {
        this.cast = cast;
    }

}
