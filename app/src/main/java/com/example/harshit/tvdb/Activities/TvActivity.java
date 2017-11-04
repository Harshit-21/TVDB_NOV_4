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
import com.example.harshit.tvdb.Interfaces.SliderClick;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_Genre;
import com.example.harshit.tvdb.Pojo.Bean_MovieResponse;
import com.example.harshit.tvdb.Pojo.Bean_SliderImages;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvActivity extends AppCompatActivity implements RecylerClickEvents, SliderClick {

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<Bean_SliderImages> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    // this are the views
    private RecyclerView recyler_tvgenre, recyler_tv;
    private ProgressBar progress_tvgenre;
    private GenreAdapter genreAdapter;
    private MovieListAdapter listAdapter;
    private HashMap<String, String> hashmap_tv = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        initViews();
        getSliderImages();
        setSliderAdapter();
        addBottomDots(0);
        autoScrollSlider();
        getTvList();
    }

    private void getTvList() {
        if (AppUtil.isNetworkAvailable(this)) {

            ArrayList<Bean_Genre> tv_genrelist = new ArrayList<>();

            hashmap_tv.put("Tv Airing Today", getString(R.string.airing_today));
            hashmap_tv.put("TV On The Air", getString(R.string.on_the_air));
            hashmap_tv.put("Popular", getString(R.string.popular));
            hashmap_tv.put("Top Rated", getString(R.string.top_rated));

// noew we have to itearte a hashmap and put values into the array;ist

            Iterator it = hashmap_tv.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Bean_Genre bean_genre = new Bean_Genre();
                bean_genre.setName(String.valueOf(pair.getKey()));
                bean_genre.setId(0);
                bean_genre.setUrl(String.valueOf(pair.getValue()));
                tv_genrelist.add(bean_genre);
//                    System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            recyler_tv.setVisibility(View.VISIBLE);
            genreAdapter = new GenreAdapter(tv_genrelist, TvActivity.this, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            recyler_tv.setLayoutManager(mLayoutManager);
            recyler_tv.setItemAnimator(new DefaultItemAnimator());
            recyler_tv.setAdapter(genreAdapter);

        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
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
        Integer images_array[] = new Integer[]{R.drawable.ic_menu_manage, R.drawable.ic_menu_manage, R.drawable.ic_menu_manage, R.drawable.ic_menu_manage};
        String images_title[] = new String[]{"now_playing", "popular", "top_rated", "upcoming"};


        for (int i = 0; i < 4; i++) {
            Bean_SliderImages bean_sliderImages = new Bean_SliderImages();
            bean_sliderImages.setId(images_array[i]);
            bean_sliderImages.setName(images_title[i]);
            slider_image_list.add(bean_sliderImages);
        }

    }

    private void initViews() {
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        recyler_tv = (RecyclerView) findViewById(R.id.recyler_tv);
        recyler_tvgenre = (RecyclerView) findViewById(R.id.recyler_tvgenre);
        progress_tvgenre = (ProgressBar) findViewById(R.id.progress_tvgenre);

    }


    private void setSliderAdapter() {


        sliderPagerAdapter = new SliderPagerAdapter(this, slider_image_list,this);
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

            case "tv_click":
                if (object != null) {
                    Intent movie_detailIntent = new Intent(this, TvDetailActivity.class);
                    movie_detailIntent.putExtra("TV_DETAILS", (Serializable) object);
                    startActivity(movie_detailIntent);
                }
                break;
            case "genre_click":
                if (object != null) {
                    Bean_Genre bean_genre = (Bean_Genre) object;

                    if (recyler_tvgenre.getVisibility() == View.VISIBLE) {
                        recyler_tvgenre.setVisibility(View.GONE);
                    }
                    progress_tvgenre.setVisibility(View.VISIBLE);
                    getListAccordingtoType(bean_genre.getUrl());
                } else {
                    AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
                    finish();
                }
                break;

        }


    }

    private void getListAccordingtoType(String tv_type) {
        // here we have to server hit
        if (AppUtil.isNetworkAvailable(this)) {
            MyApplication application = (MyApplication) getApplication();
            if (application != null) {

                Call<Bean_MovieResponse> call = null;
                switch (tv_type.toLowerCase().trim()) {
                    case "popular":
                        call = application.getRetrofitInstance().getPopularTv(AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                        break;
                    case "top_rated":
                        call = application.getRetrofitInstance().getTopRatedTv(AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                        break;
                    case "on_the_air":
                        call = application.getRetrofitInstance().getOnTheAirTv(AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                        break;
                    case "airing_today":
                        call = application.getRetrofitInstance().getAiringToday(AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                        break;
                }

                call.enqueue(new Callback<Bean_MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Bean_MovieResponse> call, @NonNull Response<Bean_MovieResponse> response) {

                        if (!response.message().isEmpty()) {
                            ArrayList<Bean_MovieDetails> bean_List = response.body().getResults();
                            handleTvListData(bean_List);
                        }
                    }

                    @Override
                    public void onFailure(Call<Bean_MovieResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("ERROR", t.toString());
                        AppUtil.openNonInternetActivity(TvActivity.this, getResources().getString(R.string.something_went_wrong));
                        finish();
                    }
                });
            }
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
            finish();
        }
    }

    private void handleTvListData(ArrayList<Bean_MovieDetails> bean_details) {
        if (bean_details != null) {
            progress_tvgenre.setVisibility(View.GONE);
            recyler_tvgenre.setVisibility(View.VISIBLE);
            listAdapter = new MovieListAdapter(bean_details, TvActivity.this, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyler_tvgenre.setLayoutManager(mLayoutManager);
            recyler_tvgenre.setItemAnimator(new DefaultItemAnimator());
            recyler_tvgenre.setAdapter(listAdapter);
        } else {
            progress_tvgenre.setVisibility(View.GONE);
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }


    @Override
    public void onClick(String name, Object object, String tag) {

    }
}
