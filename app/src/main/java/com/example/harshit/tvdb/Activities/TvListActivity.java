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
import com.example.harshit.tvdb.Adapters.TvListAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Interfaces.SliderClick;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_MovieResponse;
import com.example.harshit.tvdb.Pojo.Bean_SliderImages;
import com.example.harshit.tvdb.Pojo.Bean_TvDetails;
import com.example.harshit.tvdb.Pojo.Bean_TvList;
import com.example.harshit.tvdb.Pojo.Bean_TvResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvListActivity extends AppCompatActivity implements View.OnClickListener, RecylerClickEvents, SliderClick {

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<Bean_SliderImages> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    // this are the views
    private RecyclerView recyler_tv;
    private ProgressBar progress_tv;
    private HashMap<String, String> hashmap_tv = new HashMap<>();
    private int total_pages;
    private int page_no = 1;
    private LinearLayout ll_page;
    private TextView tv_previous, tv_next;
    private TvListAdapter tvAdapter;
    private String tv_cat = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        initViews();
        setListners();
        getSliderImages();
        setSliderAdapter();
        addBottomDots(0);
        autoScrollSlider();
        getListAccordingtoType(AppConstant.TV_POPULAR);
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
        Integer images_array[] = new Integer[]{R.drawable.popular_ico, R.drawable.top_rated_ico, R.drawable.tv_airing_ico, R.drawable.tv_on_the_air_ico};
        String images_title[] = new String[]{AppConstant.TV_POPULAR, AppConstant.TV_TOP_RATED, AppConstant.TV_AIRING, AppConstant.TV_ON_THE_AIR};

        for (int i = 0; i < 4; i++) {
            Bean_SliderImages bean_sliderImages = new Bean_SliderImages();
            bean_sliderImages.setId(images_array[i]);
            bean_sliderImages.setName(images_title[i]);
            slider_image_list.add(bean_sliderImages);
        }

    }

    private void initViews() {
        vp_slider = findViewById(R.id.vp_slider);
        ll_dots = findViewById(R.id.ll_dots);
        recyler_tv = findViewById(R.id.recyler_tv);
        ll_page = findViewById(R.id.ll_page);
        tv_previous = findViewById(R.id.tv_previousTv);
        tv_next = findViewById(R.id.tv_nextTv);
        progress_tv = findViewById(R.id.progress_tv);
    }


    private void setSliderAdapter() {


        sliderPagerAdapter = new SliderPagerAdapter(this, slider_image_list, this);
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
        if (value != null) {
            Intent movie_detailIntent = new Intent(this, TvDetailActivity.class);
            movie_detailIntent.putExtra("TV_DETAILS", value);
            startActivity(movie_detailIntent);
        }
    }

    private void getListAccordingtoType(String tv_type) {
        // here we have to server hit
        tv_cat = tv_type;
        if (AppUtil.isNetworkAvailable(this)) {
            MyApplication application = (MyApplication) getApplication();
            if (application != null) {
                progress_tv.setVisibility(View.VISIBLE);
                recyler_tv.setVisibility(View.GONE);
                ll_page.setVisibility(View.GONE);
                Call<Bean_TvResponse> call = null;
                switch (tv_type.toLowerCase().trim()) {
                    case AppConstant.TV_POPULAR:
                        call = application.getRetrofitInstance().getPopularTv(AppConstant.API_KEY, AppConstant.ENG_LANGUAGE, page_no);
                        break;
                    case AppConstant.TV_TOP_RATED:
                        call = application.getRetrofitInstance().getTopRatedTv(AppConstant.API_KEY, AppConstant.ENG_LANGUAGE, page_no);
                        break;
                    case AppConstant.TV_AIRING:
                        call = application.getRetrofitInstance().getAiringToday(AppConstant.API_KEY, AppConstant.ENG_LANGUAGE, page_no);
                        break;
                    case AppConstant.TV_ON_THE_AIR:
                        call = application.getRetrofitInstance().getOnTheAirTv(AppConstant.API_KEY, AppConstant.ENG_LANGUAGE, page_no);
                        break;
                }

                call.enqueue(new Callback<Bean_TvResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Bean_TvResponse> call, @NonNull Response<Bean_TvResponse> response) {

                        progress_tv.setVisibility(View.GONE);
                        if (!response.message().isEmpty()) {
                            ArrayList<Bean_TvList> bean_List = response.body().getResults();
                            total_pages = response.body().getTotalPages();
                            handleTvData(bean_List, total_pages);

                        }
                    }

                    @Override
                    public void onFailure(Call<Bean_TvResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("ERROR", t.toString());
                        progress_tv.setVisibility(View.GONE);
                        AppUtil.openNonInternetActivity(TvListActivity.this, getResources().getString(R.string.something_went_wrong));
                        finish();
                    }
                });
            }
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
            finish();
        }
    }

    private void handleTvData(ArrayList<Bean_TvList> bean_list, int total_pages) {
        if (bean_list != null) {
            recyler_tv.setVisibility(View.VISIBLE);
            if (total_pages > 1) {
                ll_page.setVisibility(View.VISIBLE);
            }
            tvAdapter = new TvListAdapter(bean_list, TvListActivity.this, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyler_tv.setLayoutManager(mLayoutManager);
            recyler_tv.setItemAnimator(new DefaultItemAnimator());
            recyler_tv.setAdapter(tvAdapter);

        } else {
            AppUtil.openNonInternetActivity(TvListActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void setListners() {
        tv_previous.setOnClickListener(this);
        tv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_previousTv:
                if (page_no > 1)
                    page_no--;
                else
                    page_no = 1;
                handlePages(page_no);
                getListAccordingtoType(tv_cat);
                break;
            case R.id.tv_nextTv:
                page_no++;
                handlePages(page_no);
                getListAccordingtoType(tv_cat);
                break;
        }
    }

    private void handlePages(int page_no) {
        if (page_no == 1) {
            tv_previous.setVisibility(View.GONE);
        } else if (page_no > total_pages) {
            tv_next.setVisibility(View.GONE);
        } else {
            tv_next.setVisibility(View.VISIBLE);
            tv_previous.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(String name, Object object, String tag) {
        if (name != null) {
            getListAccordingtoType(name);
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }


}
