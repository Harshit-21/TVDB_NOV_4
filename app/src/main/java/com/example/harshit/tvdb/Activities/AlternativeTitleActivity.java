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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harshit.tvdb.Adapters.AlternativeAdapter;
import com.example.harshit.tvdb.Adapters.ReleaseDateAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Pojo.Bean_Alternatives;
import com.example.harshit.tvdb.Pojo.Bean_Translations;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlternativeTitleActivity extends AppCompatActivity {

    private LinearLayout ll_main;
    private ProgressBar progress_alternative;
    private String movie_id;
    private ArrayList<Bean_Translations> arr_alternativeTitle = new ArrayList<>();
    private AlternativeAdapter alternativeAdapter;
    private RecyclerView recyler_alternative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_title);
        AppUtil.setActionBar(this);
        getDataFromBundle();
        initViews();
        getAlternativeMovies();
    }

    private void getAlternativeMovies() {
        if (movie_id != null) {
            if (AppUtil.isNetworkAvailable(this)) {
                MyApplication application = (MyApplication) getApplication();
                if (application != null) {
                    progress_alternative.setVisibility(View.VISIBLE);
                    Call<Bean_Alternatives> call = application.getRetrofitInstance().getAlternativeTitles(Integer.parseInt(movie_id), AppConstant.API_KEY);
                    call.enqueue(new Callback<Bean_Alternatives>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_Alternatives> call, @NonNull Response<Bean_Alternatives> response) {
                            if (!response.message().isEmpty()) {
                                final ArrayList<Bean_Translations> arr_alternaative = response.body().getTitles();
                                // now this arraylist has iso name as well as josn array
                                progress_alternative.setVisibility(View.GONE);

//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
                                        handleData(arr_alternaative);

//                                    }
//                                }).start();


                                // now we have to make a hashmap regarding this
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_Alternatives> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("ERROR", t.toString());
                            AppUtil.openNonInternetActivity(AlternativeTitleActivity.this, getResources().getString(R.string.something_went_wrong));
                            finish();
                        }
                    });


                }
            } else {
                AppUtil.openNonInternetActivity(AlternativeTitleActivity.this, getResources().getString(R.string.no_internet));
                finish();
            }
        } else {
            AppUtil.openNonInternetActivity(AlternativeTitleActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void handleData(ArrayList<Bean_Translations> arr_alternative) {
        if (arr_alternative != null && MovieDetailActivity.arr_translations != null) {

            for (int i = 0; i < arr_alternative.size(); i++) {
                Bean_Translations bean_translations = new Bean_Translations();
                String iso = arr_alternative.get(i).getIso31661();
                String english_name = "";
                // we have to traverse into the transaltions
                for (int j = 0; j < MovieDetailActivity.arr_translations.size(); j++) {
                    if (MovieDetailActivity.arr_translations.get(j).getIso31661().equalsIgnoreCase(iso)) {
                        english_name = MovieDetailActivity.arr_translations.get(j).getEnglishName();
                        break;
                    }
                }
                bean_translations.setEnglishName(english_name);
                bean_translations.setTitle(arr_alternative.get(i).getTitle());

                arr_alternativeTitle.add(bean_translations);
            }

            // here we hve to set the adapter
            recyler_alternative.setVisibility(View.VISIBLE);
            alternativeAdapter = new AlternativeAdapter(arr_alternativeTitle, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyler_alternative.setLayoutManager(mLayoutManager);
            recyler_alternative.setItemAnimator(new DefaultItemAnimator());
            recyler_alternative.setAdapter(alternativeAdapter);


        } else {
            AppUtil.openNonInternetActivity(AlternativeTitleActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }

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

    private void initViews() {
        ll_main = findViewById(R.id.ll_main);
        progress_alternative = findViewById(R.id.progress_alternative);
        recyler_alternative = findViewById(R.id.recyler_alternative);

    }

    private void getDataFromBundle() {
        movie_id = getIntent().getStringExtra("MOVIE_ID");
        Log.d("MOVIE_ID", movie_id + "");

    }
}
