package com.example.harshit.tvdb.Activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harshit.tvdb.Adapters.EpisodeAdapter;
import com.example.harshit.tvdb.Adapters.MovieListAdapter;
import com.example.harshit.tvdb.Adapters.SeasonNumberAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Pojo.Bean_Episode;
import com.example.harshit.tvdb.Pojo.Bean_TvDetails;
import com.example.harshit.tvdb.Pojo.Bean_TvSeasonResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvSeasonEpisodesActivity extends AppCompatActivity implements RecylerClickEvents {
    RecyclerView recyler_season, recyler_episodes;
    private String no_of_seasons = "";
    ProgressBar progress_seasonTv;
    private SeasonNumberAdapter seasonNumberAdapter;
    private String tv_id = "";
    FrameLayout fl_tvImage;
    TextView tv_seasonNo, tv_airDate;
    private EpisodeAdapter episodeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_season_episodes);
        AppUtil.setActionBar(this);

        getDataFromBundle();
        initViews();
        getNoOfSeasonsNumber();
    }

    private void getNoOfSeasonsNumber() {
        final ArrayList<Integer> seasonList = new ArrayList<>();
        int season_count = Integer.valueOf(no_of_seasons);

        for (int i = 1; i <= season_count; i++) {
            seasonList.add(i);
        }

        seasonNumberAdapter = new SeasonNumberAdapter(seasonList, getApplicationContext(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyler_season.setLayoutManager(mLayoutManager);
        recyler_season.setItemAnimator(new DefaultItemAnimator());
        recyler_season.setAdapter(seasonNumberAdapter);

        // this is used to set the auto click for the recylerview item
        recyler_season.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (seasonList.size() > 0) {
                    recyler_season.findViewHolderForAdapterPosition(0).itemView.performClick();
                }
            }
        }, 100);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void getDetailsOfTv(int season_no) {
        if (tv_id != null) {
            Log.e("TV_ID", tv_id);
            if (AppUtil.isNetworkAvailable(this)) {
                MyApplication application = (MyApplication) getApplication();
                if (application != null) {
                    progress_seasonTv.setVisibility(View.VISIBLE);
                    recyler_episodes.setVisibility(View.GONE);
                    Call<Bean_TvSeasonResponse> call = application.getRetrofitInstance().getTvSeasonEpisodes(Integer.parseInt(tv_id), season_no, AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                    call.enqueue(new Callback<Bean_TvSeasonResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_TvSeasonResponse> call, @NonNull Response<Bean_TvSeasonResponse> response) {

                            progress_seasonTv.setVisibility(View.GONE);
                            if (!response.message().isEmpty()) {
                                ArrayList<Bean_Episode> arr_episode = response.body().getEpisodes();


                                Picasso.with(getApplicationContext()).load(AppConstant.IMG_PATH + response.body().getPosterPath()).error(getApplicationContext().getResources().getDrawable(R.drawable.something_went_wrong)).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                                        fl_tvImage.setBackgroundDrawable(ob);

                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });


                                tv_airDate.setText(!TextUtils.isEmpty(response.body().getAirDate()) ? "AIR ON: " + response.body().getAirDate() : "");
                                tv_seasonNo.setText(!TextUtils.isEmpty(String.valueOf(response.body().getSeasonNumber())) ? "Season No: " + response.body().getSeasonNumber() : "");

                                handleEpisodeData(arr_episode);
                                // we get the episode list and we need to set into the adapter
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_TvSeasonResponse> call, Throwable t) {
                            // Log error here since request failed
                            progress_seasonTv.setVisibility(View.GONE);
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

    private void handleEpisodeData(ArrayList<Bean_Episode> arr_episode) {
        if (arr_episode != null) {
            recyler_episodes.setVisibility(View.VISIBLE);
            episodeAdapter = new EpisodeAdapter(arr_episode, getApplicationContext(), this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyler_episodes.setLayoutManager(mLayoutManager);
            recyler_episodes.setItemAnimator(new DefaultItemAnimator());
            recyler_episodes.setAdapter(episodeAdapter);
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void getDataFromBundle() {
        no_of_seasons = getIntent().getStringExtra("NO_OF_SEASONS");
        tv_id = getIntent().getStringExtra("TV_ID");
    }

    private void initViews() {
        recyler_season = findViewById(R.id.recyler_season);
        progress_seasonTv = findViewById(R.id.progress_seasonTv);
        recyler_episodes = findViewById(R.id.recyler_episodes);
        fl_tvImage = findViewById(R.id.fl_tvImage);
        tv_seasonNo = findViewById(R.id.tv_seasonNo);
        tv_airDate = findViewById(R.id.tv_airDate);
    }

    @Override
    public void OnClick(int position, String value, Object object, String TAG) {
        if (value != null) {
            int season_no = Integer.parseInt(value);
            getDetailsOfTv(season_no);


        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }
}
