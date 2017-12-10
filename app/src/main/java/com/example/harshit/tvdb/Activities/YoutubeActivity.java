package com.example.harshit.tvdb.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_Video;
import com.example.harshit.tvdb.Pojo.Bean_YoutubeResponse;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YoutubeActivity extends YouTubeBaseActivity {

    private String movie_id;
    private YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "286436370914-9jrd5u15vdnloj1oj48nv6ds5qf5l72o";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private ArrayList<Bean_Video> arr_videos;
    private String coming_from;
    private String tv_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        AppUtil.setActionBar(this);
        getDataFromBundle();
        movie_id = getIntent().getStringExtra("MOVIE_ID");
        getYoutubeVideos();

    }

    private void getDataFromBundle() {

        coming_from = getIntent().getStringExtra("COMING_FROM");
        if (coming_from != null && coming_from.equalsIgnoreCase("movie")) {
            movie_id = getIntent().getStringExtra("MOVIE_ID");
        } else {
            tv_id = getIntent().getStringExtra("TV_ID");
        }
    }

    private void getYoutubeVideos() {


        if (AppUtil.isNetworkAvailable(this)) {
            MyApplication application = (MyApplication) getApplication();
            if (application != null) {
                Call<Bean_YoutubeResponse> call = null;
                if (coming_from.equalsIgnoreCase("movie")) {
                    call = application.getRetrofitInstance().getVideos(Integer.parseInt(movie_id), AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                } else {
                    call = application.getRetrofitInstance().getTvVideos(Integer.parseInt(tv_id), AppConstant.API_KEY, AppConstant.ENG_LANGUAGE);
                }


                call.enqueue(new Callback<Bean_YoutubeResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Bean_YoutubeResponse> call, @NonNull Response<Bean_YoutubeResponse> response) {
                        if (!response.message().isEmpty()) {
                            arr_videos = response.body().getResults();
                            YouTubePlayerView youTubeView = findViewById(R.id.youtube_view);
                            youTubeView.initialize(YoutubeDeveloperKey, new YouTubePlayer.OnInitializedListener() {
                                @TargetApi(Build.VERSION_CODES.N)
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

                                    YPlayer = player;
 /*
 * Now that this variable YPlayer is global you can access it
 * throughout the activity, and perform all the player actions like
 * play, pause and seeking to a position by code.
*/
                                    if (!wasRestored) {
                                        if (arr_videos != null) {

                                            ArrayList<String> arr_key = new ArrayList<>();
                                            // as this is the bean class we need string for load videos methof
                                            for (Bean_Video bean_video : arr_videos) {

                                                arr_key.add(bean_video.getKey());
                                            }

                                            YPlayer.loadVideos(arr_key);
//                                                YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

                                        }

                                    } else {
                                        AppUtil.openNonInternetActivity(YoutubeActivity.this, getResources().getString(R.string.no_internet));
                                        finish();
                                    }
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider
                                                                            provider, YouTubeInitializationResult errorReason) {
                                    if (errorReason.isUserRecoverableError()) {
                                        errorReason.getErrorDialog(YoutubeActivity.this, RECOVERY_DIALOG_REQUEST).show();
                                    } else {
                                        String errorMessage = String.format(
                                                "There was an error initializing the YouTubePlayer",
                                                errorReason.toString());
                                        Toast.makeText(YoutubeActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Bean_YoutubeResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("ERROR", t.toString());
                        AppUtil.openNonInternetActivity(YoutubeActivity.this, getResources().getString(R.string.something_went_wrong));
                        finish();
                    }
                });


            }
        } else {
            AppUtil.openNonInternetActivity(this, getResources().getString(R.string.something_went_wrong));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YoutubeDeveloperKey, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }


}
