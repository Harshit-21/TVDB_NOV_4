package com.example.harshit.tvdb.Application;

import android.app.Application;

import com.example.harshit.tvdb.Interfaces.ApiClient;
import com.example.harshit.tvdb.Interfaces.ApiInterface;

/**
 * Created by Harshit on 9/16/2017.
 */

public class MyApplication extends Application {
    public static MyApplication application;

// this class is instancied at the time of creating package in the android

    public  ApiInterface getRetrofitInstance() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService;
    }


}
