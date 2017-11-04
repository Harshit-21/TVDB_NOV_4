package com.example.harshit.tvdb.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.harshit.tvdb.Adapters.CastnCrewAdapter;
import com.example.harshit.tvdb.Adapters.MovieListAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Pojo.Bean_CastnCrew;
import com.example.harshit.tvdb.Pojo.Bean_CastnCrewResponse;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_MovieResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrewActivity extends AppCompatActivity implements RecylerClickEvents {
    String movie_id = "";
    private ProgressBar progress_crew;
    private RecyclerView recyler_crew;
    private CastnCrewAdapter castncrewadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);
        getDataFromBundle();
        initViews();
        getCastnCrewDetails();
    }

    private void initViews() {
        progress_crew = findViewById(R.id.progress_crew);
        recyler_crew = findViewById(R.id.recyler_crew);
    }

    private void getCastnCrewDetails() {
        if (movie_id != null) {
            if (AppUtil.isNetworkAvailable(this)) {

                progress_crew.setVisibility(View.VISIBLE);
                MyApplication application = (MyApplication) getApplication();
                if (application != null) {
                    Call<Bean_CastnCrewResponse> call = application.getRetrofitInstance().getCredits(movie_id, AppConstant.API_KEY);
                    call.enqueue(new Callback<Bean_CastnCrewResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_CastnCrewResponse> call, @NonNull Response<Bean_CastnCrewResponse> response) {

                            if (!response.message().isEmpty()) {
                                ArrayList<Bean_CastnCrew> arr_castncrew = response.body().getCast();
                                handleData(arr_castncrew);

                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_CastnCrewResponse> call, Throwable t) {
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

    private void handleData(ArrayList<Bean_CastnCrew> arr_castncrew) {
        if (arr_castncrew != null) {
            progress_crew.setVisibility(View.GONE);
            recyler_crew.setVisibility(View.VISIBLE);

            castncrewadapter = new CastnCrewAdapter(arr_castncrew,getApplicationContext(), this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyler_crew.setLayoutManager(mLayoutManager);
            recyler_crew.setItemAnimator(new DefaultItemAnimator());
            recyler_crew.setAdapter(castncrewadapter);


        } else {
            progress_crew.setVisibility(View.GONE);
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }


    private void getDataFromBundle() {
        movie_id = getIntent().getStringExtra("MOVIE_ID");
        Log.d("MOVIE_ID", movie_id);
    }

    @Override
    public void OnClick(int position, String value, Object object, String TAG) {

    }
}