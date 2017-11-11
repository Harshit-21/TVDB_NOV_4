package com.example.harshit.tvdb.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.harshit.tvdb.Activities.MovieDetailActivity;
import com.example.harshit.tvdb.Activities.MoviesListActivity;
import com.example.harshit.tvdb.Adapters.GenreAdapter;
import com.example.harshit.tvdb.Adapters.ReleaseDateAdapter;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_Release;
import com.example.harshit.tvdb.Pojo.Bean_ReleaseDateResponse;
import com.example.harshit.tvdb.Pojo.Bean_Translations;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.harshit.tvdb.Utils.AppConstant.API_KEY;


public class ReleaseDateFragment extends Fragment {
    private RecyclerView recyler_releaseDate;
    private ProgressBar progress_release;
    private ImageView img_close;
    private int movie_id;
    private LinkedHashMap<String, ArrayList<Bean_Translations>> hash_translations = null;
    private ReleaseDateAdapter releaseAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_release_date, container, false);
        getDataFromBundle();
        initViews(view);
        getReleaseDateFromServer();
        return view;
    }

    private void getReleaseDateFromServer() {
        if (movie_id != 0) {
            if (AppUtil.isNetworkAvailable(getActivity())) {
                MyApplication application = (MyApplication) getActivity().getApplication();
                if (application != null) {
                    progress_release.setVisibility(View.VISIBLE);
                    Call<Bean_ReleaseDateResponse> call = application.getRetrofitInstance().getReleaseDates(movie_id, AppConstant.API_KEY);
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
                            AppUtil.openNonInternetActivity(getActivity(), getResources().getString(R.string.something_went_wrong));
                            getActivity().finish();
                        }
                    });


                }
            } else {
                AppUtil.openNonInternetActivity(getActivity(), getResources().getString(R.string.no_internet));
                getActivity().finish();
            }
        } else {
            AppUtil.openNonInternetActivity(getActivity(), getResources().getString(R.string.something_went_wrong));
            getActivity().finish();
        }
    }

    private void makeHashmap(final ArrayList<Bean_Release> arr_release) {
            if (arr_release != null) {
            progress_release.setVisibility(View.GONE);
                        recyler_releaseDate.setVisibility(View.VISIBLE);
                        releaseAdapter = new ReleaseDateAdapter(arr_release, getActivity());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyler_releaseDate.setLayoutManager(mLayoutManager);
                        recyler_releaseDate.setItemAnimator(new DefaultItemAnimator());
                        recyler_releaseDate.setAdapter(releaseAdapter);
        } else {
            AppUtil.openNonInternetActivity(getActivity(), getResources().getString(R.string.something_went_wrong));
            getActivity().finish();
        }
    }

    private void getDataFromBundle() {
        movie_id = Integer.parseInt(getArguments().getString("MOVIE_ID"));
        Log.d("MOVIE_ID", movie_id + "");

    }

    private void initViews(View view) {
        recyler_releaseDate = view.findViewById(R.id.recyler_releaseDate);
        progress_release = view.findViewById(R.id.progress_release);
        img_close = view.findViewById(R.id.img_close);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}
