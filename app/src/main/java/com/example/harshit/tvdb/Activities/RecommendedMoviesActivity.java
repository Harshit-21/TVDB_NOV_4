package com.example.harshit.tvdb.Activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.harshit.tvdb.Adapters.MovieListAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_MovieResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedMoviesActivity extends AppCompatActivity implements RecylerClickEvents {
    private RecyclerView recyler_recommendedMovies;
    private String movie_id = "";
    private MovieListAdapter movieAdapter;
    private ProgressBar progress_recommended;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_movies);
        getDataFromBundle();
        initViews();
        getRecommendedMovies();
    }

    private void getDataFromBundle() {
        movie_id = getIntent().getStringExtra("MOVIE_ID");
        Log.d("MOVIE_ID",movie_id);
    }

    private void getRecommendedMovies() {
        if (movie_id != null) {
            if (AppUtil.isNetworkAvailable(this)) {

                progress_recommended.setVisibility(View.VISIBLE);
                MyApplication application = (MyApplication) getApplication();
                if (application != null) {
                    Call<Bean_MovieResponse> call = application.getRetrofitInstance().getRecommendedMovies(movie_id, AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                    call.enqueue(new Callback<Bean_MovieResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_MovieResponse> call, @NonNull Response<Bean_MovieResponse> response) {

                            if (!response.message().isEmpty()) {
                                ArrayList<Bean_MovieDetails> bean_movieList = response.body().getResults();
                                handleMoviesList(bean_movieList);
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_MovieResponse> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("ERROR", t.toString());
                            AppUtil.openNonInternetActivity(getApplicationContext(), getResources().getString(R.string.something_went_wrong));
                            finish();
                        }
                    });


                }
            } else {
                AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
                finish();
            }
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }

    }

    private void handleMoviesList(ArrayList<Bean_MovieDetails> bean_movieList) {

        if (bean_movieList != null) {
            progress_recommended.setVisibility(View.GONE);
            recyler_recommendedMovies.setVisibility(View.VISIBLE);
            movieAdapter = new MovieListAdapter(bean_movieList, getApplicationContext(), this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyler_recommendedMovies.setLayoutManager(mLayoutManager);
            recyler_recommendedMovies.setItemAnimator(new DefaultItemAnimator());
            recyler_recommendedMovies.setAdapter(movieAdapter);
        } else {
            progress_recommended.setVisibility(View.GONE);
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }


    }

    private void initViews() {
        recyler_recommendedMovies = findViewById(R.id.recyler_recommendedMovies);
        progress_recommended = findViewById(R.id.progress_recommended);
    }

    @Override
    public void OnClick(int position, String value, Object object, String TAG) {

    }
}
