package com.example.harshit.tvdb.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harshit.tvdb.Adapters.GenreAdapter;
import com.example.harshit.tvdb.Adapters.MovieListAdapter;
import com.example.harshit.tvdb.Adapters.SliderPagerAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Pojo.Bean_Genre;
import com.example.harshit.tvdb.Pojo.Bean_GenreResponse;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_MovieResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesListActivity extends AppCompatActivity implements RecylerClickEvents {

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<Integer> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    // this are the views
    private RecyclerView recyler_genre, recyler_moviegenre;
    private ProgressBar progress_moviegenre, progress_genre;
    private GenreAdapter genreAdapter;
    private MovieListAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        // method for initialisation
        initViews();
        getSliderImages();
        setSliderAdapter();
        addBottomDots(0);
        autoScrollSlider();
        getGenreList();
    }

    private void getGenreList() {
        if (AppUtil.isNetworkAvailable(this)) {
            MyApplication application = (MyApplication) getApplication();
            if (application != null) {
                Call<Bean_GenreResponse> call = application.getRetrofitInstance().getGenreList(AppConstant.API_KEY);
                call.enqueue(new Callback<Bean_GenreResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Bean_GenreResponse> call, @NonNull Response<Bean_GenreResponse> response) {

                        if (!response.message().isEmpty()) {
                            ArrayList<Bean_Genre> bean_genreList = response.body().getGenres();
                            handleGenreData(bean_genreList);
                        }
                    }

                    @Override
                    public void onFailure(Call<Bean_GenreResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("ERROR", t.toString());
                        AppUtil.openNonInternetActivity(MoviesListActivity.this, getResources().getString(R.string.something_went_wrong));
                        finish();
                    }
                });


            }
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
            finish();
        }
    }

    private void handleGenreData(ArrayList<Bean_Genre> genrelist) {
        if (genrelist != null) {
            progress_genre.setVisibility(View.GONE);
            recyler_genre.setVisibility(View.VISIBLE);
            genreAdapter = new GenreAdapter(genrelist, MoviesListActivity.this, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            recyler_genre.setLayoutManager(mLayoutManager);
            recyler_genre.setItemAnimator(new DefaultItemAnimator());
            recyler_genre.setAdapter(genreAdapter);
        } else {
            progress_genre.setVisibility(View.GONE);
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void autoScrollSlider() {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);

    }

    private void getSliderImages() {
        slider_image_list = new ArrayList<>();

//Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
// here i am adding few sample image links, you can add your own

        slider_image_list.add(R.drawable.ic_menu_camera);
        slider_image_list.add(R.drawable.ic_menu_manage);
        slider_image_list.add(R.drawable.ic_menu_share);
        slider_image_list.add(R.drawable.ic_menu_slideshow);


    }

    private void initViews() {
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        recyler_genre = (RecyclerView) findViewById(R.id.recyler_genre);
        recyler_moviegenre = (RecyclerView) findViewById(R.id.recyler_moviegenre);
        progress_moviegenre = (ProgressBar) findViewById(R.id.progress_moviegenre);
        progress_genre = (ProgressBar) findViewById(R.id.progress_genre);

    }


    private void setSliderAdapter() {


        sliderPagerAdapter = new SliderPagerAdapter(this, slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }


    @Override
    public void OnClick(int position, String value, Object object, String TAG) {


        switch (TAG.toLowerCase().trim()) {

            case "movie_click":
                if (object != null) {
                    Intent movie_detailIntent = new Intent(this, MovieDetailActivity.class);
                    movie_detailIntent.putExtra("MOVIE_DETAILS", (Serializable) object);
                    startActivity(movie_detailIntent);
                }
                break;
            case "genre_click":
                if (value != null) {
                    // here we get the genre id now we have to again server hit and get movie list according to the genre
                    progress_moviegenre.setVisibility(View.VISIBLE);
                    getMovieAccordingToGenreId(value);
                } else {
                    AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
                    finish();
                }
                break;

        }


    }

    private void getMovieAccordingToGenreId(String genre_id) {
        if (AppUtil.isNetworkAvailable(this)) {
            MyApplication application = (MyApplication) getApplication();
            if (application != null) {
                Call<Bean_MovieResponse> call = application.getRetrofitInstance().getMovieAccToGenre(genre_id, AppConstant.API_KEY, AppConstant.ENG_LANGUAGE, false, AppConstant.ASC_ORDER);
                call.enqueue(new Callback<Bean_MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Bean_MovieResponse> call, @NonNull Response<Bean_MovieResponse> response) {

                        if (!response.message().isEmpty()) {
                            ArrayList<Bean_MovieDetails> bean_movieList = response.body().getResults();
                            handleMoviesAccordingToGenre(bean_movieList);
                        }
                    }

                    @Override
                    public void onFailure(Call<Bean_MovieResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("ERROR", t.toString());
                        AppUtil.openNonInternetActivity(MoviesListActivity.this, getResources().getString(R.string.something_went_wrong));
                        finish();
                    }
                });


            }
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
            finish();
        }

    }

    private void handleMoviesAccordingToGenre(ArrayList<Bean_MovieDetails> bean_movieList) {
        if (bean_movieList != null) {
            progress_moviegenre.setVisibility(View.GONE);
            recyler_moviegenre.setVisibility(View.VISIBLE);
            movieAdapter = new MovieListAdapter(bean_movieList, MoviesListActivity.this, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyler_moviegenre.setLayoutManager(mLayoutManager);
            recyler_moviegenre.setItemAnimator(new DefaultItemAnimator());
            recyler_moviegenre.setAdapter(movieAdapter);
        } else {
            progress_moviegenre.setVisibility(View.GONE);
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }
}
