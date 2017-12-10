package com.example.harshit.tvdb.Pojo;

import android.net.Uri;

/**
 * Created by harshit on 27/11/17.
 */

public class Bean_Upload {
    private String name;
    private Uri image_uri;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(Uri image_uri) {
        this.image_uri = image_uri;
    }
}
