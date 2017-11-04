package com.example.harshit.tvdb.Interfaces;

import com.example.harshit.tvdb.Utils.AppConstant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Harshit on 9/16/2017.
 */

public class ApiClient {

    private  static Retrofit retrofit = null;
//     HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//     OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstant.BASE_URL)
//                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
