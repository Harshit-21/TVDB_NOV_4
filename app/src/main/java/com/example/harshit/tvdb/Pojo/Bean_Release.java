package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Harshit on 11/11/2017.
 */

public class Bean_Release {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("release_dates")
    @Expose
    private ArrayList<Bean_Translations> releaseDates = null;

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public ArrayList<Bean_Translations> getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(ArrayList<Bean_Translations> releaseDates) {
        this.releaseDates = releaseDates;
    }
}
