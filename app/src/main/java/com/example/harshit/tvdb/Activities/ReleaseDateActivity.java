package com.example.harshit.tvdb.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.harshit.tvdb.Adapters.ReleaseDateAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Pojo.Bean_Release;
import com.example.harshit.tvdb.Pojo.Bean_ReleaseDateResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseDateActivity extends AppCompatActivity {

    private String movie_id;
    private RecyclerView recyler_releaseDate;
    private ProgressBar progress_release;
    private ReleaseDateAdapter releaseAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_date);
        getDataFromBundle();
        AppUtil.setActionBar(this);
        initViews();
        getReleaseDateFromServer();
    }

    private void getDataFromBundle() {
        movie_id = getIntent().getStringExtra("MOVIE_ID");
        Log.d("MOVIE_ID", movie_id);

    }

    private void initViews() {
        recyler_releaseDate = findViewById(R.id.recyler_releaseDate);
        progress_release = findViewById(R.id.progress_release);
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

    private void getReleaseDateFromServer() {
        if (movie_id != null) {
            if (AppUtil.isNetworkAvailable(this)) {
                MyApplication application = (MyApplication)getApplication();
                if (application != null) {
                    progress_release.setVisibility(View.VISIBLE);
                    Call<Bean_ReleaseDateResponse> call = application.getRetrofitInstance().getReleaseDates(Integer.parseInt(movie_id), AppConstant.API_KEY);
                    call.enqueue(new Callback<Bean_ReleaseDateResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_ReleaseDateResponse> call, @NonNull Response<Bean_ReleaseDateResponse> response) {
                            if (!response.message().isEmpty()) {
                                ArrayList<Bean_Release> arr_release = response.body().getResults();
                                // now this arraylist has iso name as well as josn array
                                makeHashmap(arr_release);

                                // now we have to make a hashmap regarding this
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_ReleaseDateResponse> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("ERROR", t.toString());
                            AppUtil.openNonInternetActivity(ReleaseDateActivity.this, getResources().getString(R.string.something_went_wrong));
                            finish();
                        }
                    });


                }
            } else {
                AppUtil.openNonInternetActivity(ReleaseDateActivity.this, getResources().getString(R.string.no_internet));
                finish();
            }
        } else {
            AppUtil.openNonInternetActivity(ReleaseDateActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void makeHashmap(final ArrayList<Bean_Release> arr_release) {
        if (arr_release != null) {
            progress_release.setVisibility(View.GONE);
            recyler_releaseDate.setVisibility(View.VISIBLE);
            releaseAdapter = new ReleaseDateAdapter(arr_release, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyler_releaseDate.setLayoutManager(mLayoutManager);
            recyler_releaseDate.setItemAnimator(new DefaultItemAnimator());
            recyler_releaseDate.setAdapter(releaseAdapter);
        } else {
            AppUtil.openNonInternetActivity(ReleaseDateActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }
}
