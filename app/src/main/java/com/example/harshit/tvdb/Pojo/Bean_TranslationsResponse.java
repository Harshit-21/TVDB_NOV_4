package com.example.harshit.tvdb.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Harshit on 11/4/2017.
 */

public class Bean_TranslationsResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("translations")
    @Expose
    private ArrayList<Bean_Translations> translations ;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Bean_Translations> getTranslations() {
        return translations;
    }

    public void setTranslations(ArrayList<Bean_Translations> translations) {
        this.translations = translations;
    }
}
