package com.example.harshit.tvdb.Activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harshit.tvdb.Adapters.Production_Adapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Pojo.Bean_Genre;
import com.example.harshit.tvdb.Pojo.Bean_Production;
import com.example.harshit.tvdb.Pojo.Bean_TvDetails;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.animation.Animation.INFINITE;

public class TvDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String tv_id = "";
    private ImageView img_backdropImage;
    private ImageView img_posterimage;
    private TextView tv_titleTV, tv_DescTV, tv_popularityTV, tv_AirdateTv, tv_statusTV, tv_voteavgTV, tv_seasonTV, tv_languageTV, tv_VideosTV, tv_recommendedTV, tv_crewTV, tv_genreTV, tv_viewAllTV;
    private ExpandableListView expandable_production;
    private ProgressBar progress_tvDetail;
    private Production_Adapter production_adapter;
    private CardView card_TvDetails;
    int no_of_seasons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);
        AppUtil.setActionBar(this);
        getDataFromBundle();
        initViews();
        setListners();
        getTvDetails();
        animateCard();
    }

    private void animateCard() {
        ObjectAnimator card_y = ObjectAnimator.ofFloat(card_TvDetails, View.TRANSLATION_Y, 50);
        card_y.setDuration(2500);
        card_y.setRepeatMode(ValueAnimator.REVERSE);
        card_y.setRepeatCount(ValueAnimator.INFINITE);
        card_y.setInterpolator(new AccelerateDecelerateInterpolator());
        card_y.start();
    }

    private void getTvDetails() {

        if (tv_id != null) {
            Log.e("TV_ID", tv_id);
            if (AppUtil.isNetworkAvailable(this)) {
                MyApplication application = (MyApplication) getApplication();
                if (application != null) {
                    progress_tvDetail.setVisibility(View.VISIBLE);
                    Call<Bean_TvDetails> call = application.getRetrofitInstance().getTvDetails(Integer.parseInt(tv_id), AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                    call.enqueue(new Callback<Bean_TvDetails>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_TvDetails> call, @NonNull Response<Bean_TvDetails> response) {

                            progress_tvDetail.setVisibility(View.GONE);
                            if (!response.message().isEmpty()) {
                                Bean_TvDetails bean_tvDetails = response.body();
                                handleTvDetailData(bean_tvDetails);
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_TvDetails> call, Throwable t) {
                            // Log error here since request failed
                            progress_tvDetail.setVisibility(View.GONE);
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


    private void handleTvDetailData(Bean_TvDetails bean_tvDetails) {
        if (bean_tvDetails != null) {


            if (bean_tvDetails.getBackdropPath() != null) {
                Picasso.with(this).load(AppConstant.IMG_PATH + bean_tvDetails.getBackdropPath()).error(this.getResources().getDrawable(R.drawable.something_went_wrong)).into(img_backdropImage);
            } else {
                img_backdropImage.setVisibility(View.GONE);
            }

            if (bean_tvDetails.getPosterPath() != null) {
                Picasso.with(this).load(AppConstant.IMG_PATH + bean_tvDetails.getPosterPath()).error(this.getResources().getDrawable(R.drawable.something_went_wrong)).into(img_posterimage);

            } else {
                img_posterimage.setVisibility(View.GONE);
            }


            tv_titleTV.setText(!TextUtils.isEmpty(bean_tvDetails.getOriginalName()) ? bean_tvDetails.getOriginalName() : "");
            tv_DescTV.setText(!TextUtils.isEmpty(bean_tvDetails.getOverview()) ? bean_tvDetails.getOverview() : "");
            tv_popularityTV.setText(!TextUtils.isEmpty(String.valueOf(bean_tvDetails.getPopularity())) ? String.valueOf(bean_tvDetails.getPopularity()) : "");
            tv_AirdateTv.setText(!TextUtils.isEmpty(bean_tvDetails.getFirstAirDate()) ? bean_tvDetails.getFirstAirDate() : "");
            tv_statusTV.setText(!TextUtils.isEmpty(bean_tvDetails.getStatus()) ? bean_tvDetails.getStatus() : "");
            tv_languageTV.setText(!TextUtils.isEmpty(bean_tvDetails.getOriginalLanguage()) ? bean_tvDetails.getOriginalLanguage() : "");
            tv_voteavgTV.setText(!TextUtils.isEmpty(String.valueOf(bean_tvDetails.getVoteAverage())) ? String.valueOf(bean_tvDetails.getVoteAverage()) : "");
            tv_seasonTV.setText(!TextUtils.isEmpty(String.valueOf(bean_tvDetails.getNumberOfSeasons())) ? String.valueOf(bean_tvDetails.getNumberOfSeasons()) : "");

            no_of_seasons = bean_tvDetails.getNumberOfSeasons();
            // this is foor genre list

            ArrayList<Bean_Genre> arr_tvGenre = bean_tvDetails.getGenres();
            if (arr_tvGenre != null) {
                for (Bean_Genre bean_genre : arr_tvGenre) {

                    tv_genreTV.setText(bean_genre.getName() + ",");

                }

            } else {
                tv_genreTV.setVisibility(View.GONE);
            }

            ArrayList<Bean_Production> arr_tvProduction = bean_tvDetails.getProductionCompanies();
            if (arr_tvProduction != null) {
                // we have to set the adapter into it
                final ArrayList<String> list_data_Header = new ArrayList<>();
                list_data_Header.add("Prodution Companies");
                final HashMap<String, ArrayList<Bean_Production>> hashmap_prod = new HashMap<>();


//               for(int i=0;i<arr_tvGenre.size();i++){
//                    hashmap_prod.put(list_data_Header.get(0), arr_tvProduction.get(i));
//
//                }

                production_adapter = new Production_Adapter(this, list_data_Header, hashmap_prod);
                // setting list adapter
                expandable_production.setAdapter(production_adapter);
//                // Listview Group click listener
//                expandable_production.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//                    @Override
//                    public boolean onGroupClick(ExpandableListView parent, View v,
//                                                int groupPosition, long id) {
//                        // Toast.makeText(getApplicationContext(),
//                        // "Group Clicked " + listDataHeader.get(groupPosition),
//                        // Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                });
//
//                // Listview Group expanded listener
//                expandable_production.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//                    @Override
//                    public void onGroupExpand(int groupPosition) {
//                        Toast.makeText(getApplicationContext(), list_data_Header.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                // Listview Group collasped listener
//                expandable_production.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//                    @Override
//                    public void onGroupCollapse(int groupPosition) {
//                        Toast.makeText(getApplicationContext(),
//                                list_data_Header.get(groupPosition) + " Collapsed",
//                                Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//                // Listview on child click listener
//                expandable_production.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//                    @Override
//                    public boolean onChildClick(ExpandableListView parent, View v,
//                                                int groupPosition, int childPosition, long id) {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(
//                                getApplicationContext(),
//                                list_data_Header.get(groupPosition)
//                                        + " : "
//                                        + hashmap_prod.get(
//                                        list_data_Header.get(groupPosition)).get(
//                                        childPosition), Toast.LENGTH_SHORT)
//                                .show();
//                        return false;
//                    }
//                });
            } else {

                expandable_production.setVisibility(View.GONE);
            }
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void initViews() {
        img_posterimage = findViewById(R.id.img_posterimage);
        progress_tvDetail = findViewById(R.id.progress_tvDetail);
        img_backdropImage = findViewById(R.id.img_backdropImage);
        card_TvDetails = findViewById(R.id.card_TvDetails);

        tv_titleTV = findViewById(R.id.tv_titleTV);
        tv_DescTV = findViewById(R.id.tv_DescTV);
        tv_popularityTV = findViewById(R.id.tv_popularityTV);
        tv_AirdateTv = findViewById(R.id.tv_AirdateTv);
        tv_statusTV = findViewById(R.id.tv_statusTV);
        tv_voteavgTV = findViewById(R.id.tv_voteavgTV);
        tv_seasonTV = findViewById(R.id.tv_seasonTV);
        tv_languageTV = findViewById(R.id.tv_languageTV);
        tv_VideosTV = findViewById(R.id.tv_VideosTV);
        tv_recommendedTV = findViewById(R.id.tv_recommendedTV);
        tv_crewTV = findViewById(R.id.tv_crewTV);
        expandable_production = findViewById(R.id.expandable_production);
        tv_genreTV = findViewById(R.id.tv_genreTV);
        tv_viewAllTV = findViewById(R.id.tv_viewAllTV);
    }

    private void setListners() {
        tv_viewAllTV.setOnClickListener(this);
        tv_crewTV.setOnClickListener(this);
        tv_recommendedTV.setOnClickListener(this);
        tv_VideosTV.setOnClickListener(this);
    }

    private void getDataFromBundle() {
        tv_id = getIntent().getStringExtra("TV_DETAILS");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_viewAllTV:
                openSeasonEpisodeActivity();
                break;
            case R.id.tv_crewTV:
                openCastnCrewActivity();
                break;
            case R.id.tv_recommendedTV:
                openRecommendedTv();
                break;
            case R.id.tv_VideosTV:
                AppUtil.showToast(this, "Coming soon");

                break;
        }
    }

    private void openSeasonEpisodeActivity() {
        if (no_of_seasons > 0) {
            Intent season_intent = new Intent(this, TvSeasonEpisodesActivity.class);
            season_intent.putExtra("NO_OF_SEASONS", no_of_seasons + "");
            season_intent.putExtra("TV_ID", tv_id);
            startActivity(season_intent);
        } else {
            AppUtil.showToast(this, getString(R.string.something_went_wrong));
        }
    }

    private void openCastnCrewActivity() {

        Intent crewIntent = new Intent(this, CrewActivity.class);
        crewIntent.putExtra("TV_ID", tv_id);
        crewIntent.putExtra("COMING_FROM", "tv");
        startActivity(crewIntent);

    }

    private void openRecommendedTv() {
        Intent intent_recommend = new Intent(this, RecommendedMoviesActivity.class);
        intent_recommend.putExtra("TV_ID", tv_id);
        intent_recommend.putExtra("COMING_FROM", "tv");
        startActivity(intent_recommend);
    }
}
