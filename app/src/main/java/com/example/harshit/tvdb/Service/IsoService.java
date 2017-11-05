package com.example.harshit.tvdb.Service;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.harshit.tvdb.Activities.MoviesListActivity;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Pojo.Bean_Genre;
import com.example.harshit.tvdb.Pojo.Bean_GenreResponse;
import com.example.harshit.tvdb.Pojo.Bean_Translations;
import com.example.harshit.tvdb.Pojo.Bean_TranslationsResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Harshit on 11/4/2017.
 */

public class IsoService extends Service {

    IBinder mBinder = new LocalBinder();
    String movie_id = "";
    private ArrayList<Bean_Translations> arr_transaltions;
    com.example.harshit.tvdb.Interfaces.Callback callback1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null) {
            movie_id = intent.getStringExtra("MOVIE_ID");
        }
        return mBinder;
    }


    public class LocalBinder extends Binder {
        com.example.harshit.tvdb.Interfaces.Callback callback;

        public IsoService getServerInstance() {
            return IsoService.this;
        }
    }


    public void callIsoService() {
        if (AppUtil.isNetworkAvailable(this)) {
            MyApplication application = (MyApplication) getApplication();
            if (application != null) {
                Call<Bean_TranslationsResponse> call = application.getRetrofitInstance().getDiffrentRegionsInfoOfMovie(Integer.parseInt(movie_id), AppConstant.API_KEY);
                call.enqueue(new Callback<Bean_TranslationsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Bean_TranslationsResponse> call, @NonNull Response<Bean_TranslationsResponse> response) {

                        if (!response.message().isEmpty()) {
                            arr_transaltions = response.body().getTranslations();
                        }
                    }

                    @Override
                    public void onFailure(Call<Bean_TranslationsResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("ERROR", t.toString());
                        AppUtil.openNonInternetActivity(getApplicationContext(), getResources().getString(R.string.something_went_wrong));
                    }
                });

            }
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
        }
    }


}
