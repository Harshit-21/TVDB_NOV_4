package com.example.harshit.tvdb.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harshit.tvdb.Adapters.SliderImageAdapter;
import com.example.harshit.tvdb.Adapters.SliderPagerAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Fragments.AlternativeMovieFragment;
import com.example.harshit.tvdb.Fragments.ReleaseDateFragment;
import com.example.harshit.tvdb.Fragments.YoutubeFragment;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_MovieImages;
import com.example.harshit.tvdb.Pojo.Bean_Poster;
import com.example.harshit.tvdb.Pojo.Bean_Translations;
import com.example.harshit.tvdb.Pojo.Bean_TranslationsResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Bean_MovieDetails bean_movieDetails;
    private CardView card_movieDetails;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    private TextView[] dots;
    int page_position = 0;
    private SliderImageAdapter sliderImageAdapter;
    private ImageView img_posterimage;
    private TextView tv_moviereleasedateiso, tv_moviealternativeTitles, tv_movietitle, tv_movierevenue, tv_movieruntime, tv_moviepopularity, tv_moviereleasedate, tv_moviestatus, tv_moviebudget, tv_movieVideos, tv_recommendedmovies, tv_moviecrew;
    private FrameLayout fl_container;
    boolean mBounded;
    private ProgressBar progress_movieDetail;
    public static ArrayList<Bean_Translations> arr_translations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getDataFromBundle();
        initViews();
        setListners();
        // as the retrofit is woring in the background so we can use the async way to do this
        getTransaltions();
        getMovieDetails();
        getMovieImages();
    }

    private void handleIsoView() {
        if (arr_translations != null) {
            // now the transaltions is visible

            tv_moviereleasedateiso.setVisibility(View.VISIBLE);
            tv_moviealternativeTitles.setVisibility(View.VISIBLE);
        } else {
            tv_moviereleasedateiso.setVisibility(View.GONE);
            tv_moviealternativeTitles.setVisibility(View.GONE);
        }
    }

    private void getTransaltions() {
        if (bean_movieDetails.getId() != null) {
            if (AppUtil.isNetworkAvailable(this)) {
                MyApplication application = (MyApplication) getApplication();
                if (application != null) {
                    Call<Bean_TranslationsResponse> call = application.getRetrofitInstance().getDiffrentRegionsInfoOfMovie(bean_movieDetails.getId(), AppConstant.API_KEY);
                    call.enqueue(new Callback<Bean_TranslationsResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_TranslationsResponse> call, @NonNull Response<Bean_TranslationsResponse> response) {

                            if (!response.message().isEmpty()) {
                                arr_translations = response.body().getTranslations();
                                handleIsoView();

                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_TranslationsResponse> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("ERROR", t.toString());
                            AppUtil.openNonInternetActivity(MovieDetailActivity.this, getResources().getString(R.string.something_went_wrong));
                            finish();
                        }
                    });


                }
            } else {
                AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
                finish();
            }
        }
    }

    private void getMovieImages() {
        if (bean_movieDetails.getId() != null) {
            if (AppUtil.isNetworkAvailable(this)) {
                MyApplication application = (MyApplication) getApplication();
                if (application != null) {
                    Call<Bean_MovieImages> call = application.getRetrofitInstance().getMovieImages(bean_movieDetails.getId(), AppConstant.API_KEY);
                    call.enqueue(new Callback<Bean_MovieImages>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_MovieImages> call, @NonNull Response<Bean_MovieImages> response) {

                            if (!response.message().isEmpty()) {
                                Bean_MovieImages bean_movieImages = response.body();
                                addImagesToList(bean_movieImages);
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_MovieImages> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("ERROR", t.toString());
                            AppUtil.openNonInternetActivity(MovieDetailActivity.this, getResources().getString(R.string.something_went_wrong));
                            finish();
                        }
                    });


                }
            } else {
                AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
                finish();
            }
        }
    }

    private void addImagesToList(Bean_MovieImages bean_movieImages) {
        if (bean_movieImages != null) {

            ArrayList<Bean_Poster> arrayList_poster = bean_movieImages.getPosters();

            if (arrayList_poster.size() > 10) {

                do {
                    arrayList_poster.remove(arrayList_poster.size() - 1);

                } while (arrayList_poster.size() > 10);
            }

            addSliderAdapter(arrayList_poster);
            autoScrollSlider(arrayList_poster);


        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }


    }


    private void autoScrollSlider(final ArrayList<Bean_Poster> arrayList_poster) {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == arrayList_poster.size()) {
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


    private void addSliderAdapter(final ArrayList<Bean_Poster> arrayList_poster) {
        sliderImageAdapter = new SliderImageAdapter(this, arrayList_poster);
        vp_slider.setAdapter(sliderImageAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, arrayList_poster);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void addBottomDots(int currentPage, ArrayList<Bean_Poster> arrayList_poster) {
        dots = new TextView[arrayList_poster.size()];

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


    private void getMovieDetails() {
        if (bean_movieDetails.getId() != null) {
            Log.e("MOVIE_ID", bean_movieDetails.getId() + "");
            if (AppUtil.isNetworkAvailable(this)) {
                MyApplication application = (MyApplication) getApplication();
                if (application != null) {
                    progress_movieDetail.setVisibility(View.VISIBLE);
                    Call<Bean_MovieDetails> call = application.getRetrofitInstance().getMovieDetails(bean_movieDetails.getId(), AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                    call.enqueue(new Callback<Bean_MovieDetails>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_MovieDetails> call, @NonNull Response<Bean_MovieDetails> response) {
                            if (!response.message().isEmpty()) {
                                Bean_MovieDetails bean_movieDetails = response.body();
                                handleMovieDetailData(bean_movieDetails);
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_MovieDetails> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("ERROR", t.toString());
                            AppUtil.openNonInternetActivity(MovieDetailActivity.this, getResources().getString(R.string.something_went_wrong));
                            finish();
                        }
                    });


                }
            } else {
                AppUtil.openNonInternetActivity(this, getResources().getString(R.string.no_internet));
                finish();
            }
        }
    }

    private void handleMovieDetailData(Bean_MovieDetails bean_movieDetails) {
        if (bean_movieDetails != null) {
            progress_movieDetail.setVisibility(View.GONE);
            tv_movietitle.setText(bean_movieDetails.getTitle() != null ? bean_movieDetails.getTitle() : "");
            tv_moviepopularity.setText(String.valueOf(bean_movieDetails.getPopularity() != null ? bean_movieDetails.getPopularity() : ""));
            tv_moviereleasedate.setText(!TextUtils.isEmpty(bean_movieDetails.getReleaseDate()) ? bean_movieDetails.getReleaseDate() : "");
            tv_moviestatus.setText(!TextUtils.isEmpty(bean_movieDetails.getStatus()) ? bean_movieDetails.getStatus() : "");
            tv_moviebudget.setText(AppUtil.currencyFormatter(String.valueOf(bean_movieDetails.getBudget())));
            // this is the revenue we have to put in indian currency
            tv_movierevenue.setText(AppUtil.currencyFormatter(String.valueOf(bean_movieDetails.getRevenue())));
            // we have to convert the time in minutes
            tv_movieruntime.setText(AppUtil.getFormattedTime(bean_movieDetails.getRuntime()));
            Log.e("YPUTUBE", bean_movieDetails.getVideo() + "");

        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void setListners() {
        tv_movieVideos.setOnClickListener(this);
        tv_recommendedmovies.setOnClickListener(this);
        tv_moviecrew.setOnClickListener(this);
        tv_moviealternativeTitles.setOnClickListener(this);
        tv_moviereleasedateiso.setOnClickListener(this);
    }

    private void initViews() {
        tv_movietitle = (TextView) findViewById(R.id.tv_movietitle);
        tv_moviepopularity = (TextView) findViewById(R.id.tv_moviepopularity);
        tv_moviereleasedate = (TextView) findViewById(R.id.tv_moviereleasedate);
        tv_moviestatus = (TextView) findViewById(R.id.tv_moviestatus);
        tv_moviebudget = (TextView) findViewById(R.id.tv_moviebudget);
        tv_movieVideos = (TextView) findViewById(R.id.tv_movieVideos);
        tv_recommendedmovies = (TextView) findViewById(R.id.tv_recommendedmovies);
        tv_moviereleasedateiso = (TextView) findViewById(R.id.tv_moviereleasedateiso);
        tv_moviecrew = (TextView) findViewById(R.id.tv_moviecrew);
        tv_moviealternativeTitles = (TextView) findViewById(R.id.tv_moviealternativeTitles);
        tv_movierevenue = (TextView) findViewById(R.id.tv_movierevenue);
        tv_movieruntime = (TextView) findViewById(R.id.tv_movieruntime);
        img_posterimage = (ImageView) findViewById(R.id.img_posterimage);
        card_movieDetails = (CardView) findViewById(R.id.card_movieDetails);
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        fl_container = (FrameLayout) findViewById(R.id.fl_container);
        progress_movieDetail = findViewById(R.id.progress_movieDetail);
    }

    private void getDataFromBundle() {
        bean_movieDetails = (Bean_MovieDetails) getIntent().getSerializableExtra("MOVIE_DETAILS");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_movieVideos:
//                openVideoFragment();
                break;
            case R.id.tv_recommendedmovies:
                openRecommendedMoviesActivity();
                break;

            case R.id.tv_moviecrew:
                openCrewnCastActivity();
                break;
            case R.id.tv_moviealternativeTitles:
                openAlternativeMovieFragment();
//                AppUtil.showToast(this, "coming soon");

                break;
            case R.id.tv_moviereleasedateiso:
                openReleaseDateFragment();
//                AppUtil.showToast(this, "coming soon");
                break;
        }
    }

    private void openReleaseDateFragment() {

        Bundle bundle = new Bundle();
        bundle.putString("MOVIE_ID", bean_movieDetails.getId().toString());
        //set Fragmentclass Arguments

        ReleaseDateFragment releaseDateFragment = new ReleaseDateFragment();
        releaseDateFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, releaseDateFragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
        transaction.addToBackStack(null);  // this will manage backstack
        transaction.commit();

    }

    private void openAlternativeMovieFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("MOVIE_ID", bean_movieDetails.getId().toString());
        AlternativeMovieFragment alternativeMovieFragment = new AlternativeMovieFragment();
        alternativeMovieFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, alternativeMovieFragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
        transaction.addToBackStack(null);  // this will manage backstack
        transaction.commit();
    }


    private void openCrewnCastActivity() {
        Intent crewIntent = new Intent(this, CrewActivity.class);
        crewIntent.putExtra("MOVIE_ID", String.valueOf(bean_movieDetails.getId()));
        startActivity(crewIntent);
    }

    private void openRecommendedMoviesActivity() {
        Intent recommended_movies = new Intent(this, RecommendedMoviesActivity.class);
        recommended_movies.putExtra("MOVIE_ID", String.valueOf(bean_movieDetails.getId()));
        startActivity(recommended_movies);
    }

    private void openVideoFragment() {
//        YoutubeFragment fragment = new YoutubeFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fl_container, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
//        transaction.addToBackStack(null);  // this will manage backstack
//        transaction.commit();
    }


}
