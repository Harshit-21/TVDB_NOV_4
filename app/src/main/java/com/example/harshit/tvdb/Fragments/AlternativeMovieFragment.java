package com.example.harshit.tvdb.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harshit.tvdb.Activities.MovieDetailActivity;
import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Pojo.Bean_Alternatives;
import com.example.harshit.tvdb.Pojo.Bean_Release;
import com.example.harshit.tvdb.Pojo.Bean_ReleaseDateResponse;
import com.example.harshit.tvdb.Pojo.Bean_Translations;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlternativeMovieFragment extends Fragment {
    private LinearLayout ll_main;
    private ProgressBar progress_alternative;
    private int movie_id;
    private ImageView img_close;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alternative_movie, container, false);
        getDataFromBundle();
        initViews(view);
        getAlternativeMovies();
        return view;
    }

    private void getAlternativeMovies() {
        if (movie_id != 0) {
            if (AppUtil.isNetworkAvailable(getActivity())) {
                MyApplication application = (MyApplication) getActivity().getApplication();
                if (application != null) {
                    progress_alternative.setVisibility(View.VISIBLE);
                    Call<Bean_Alternatives> call = application.getRetrofitInstance().getAlternativeTitles(movie_id, AppConstant.API_KEY);
                    call.enqueue(new Callback<Bean_Alternatives>() {
                        @Override
                        public void onResponse(@NonNull Call<Bean_Alternatives> call, @NonNull Response<Bean_Alternatives> response) {
                            if (!response.message().isEmpty()) {
                                ArrayList<Bean_Translations> arr_alternaative = response.body().getTitles();
                                // now this arraylist has iso name as well as josn array
                                progress_alternative.setVisibility(View.GONE);
                                handleData(arr_alternaative);

                                // now we have to make a hashmap regarding this
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean_Alternatives> call, Throwable t) {
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

    private void handleData(ArrayList<Bean_Translations> arr_alternative) {

        if (arr_alternative != null) {

            for (int i = 0; i < arr_alternative.size(); i++) {

                String iso = arr_alternative.get(i).getIso31661();
                String english_name = "";
                // we have to traverse into the transaltions
                for (int j = 0; j < MovieDetailActivity.arr_translations.size(); j++) {
                    if (MovieDetailActivity.arr_translations.get(j).getIso31661().equalsIgnoreCase(iso)) {
                        english_name = MovieDetailActivity.arr_translations.get(j).getEnglishName();
                        break;
                    }
                }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                // now we have to make textviews and add them dynamically
                params.addRule(RelativeLayout.RIGHT_OF, i);

                TextView textView_english_name = new TextView(getActivity());
                textView_english_name.setText(english_name);
                textView_english_name.setId(i);

                TextView textView_title = new TextView(getActivity());
                textView_title.setText(arr_alternative.get(i).getTitle());
                textView_title.setLayoutParams(params);

                ll_main.addView(textView_english_name, i);
            }


        } else {
            AppUtil.openNonInternetActivity(getActivity(), getResources().getString(R.string.something_went_wrong));
            getActivity().finish();
        }

    }

    private void initViews(View view) {
        ll_main = view.findViewById(R.id.ll_main);
        progress_alternative = view.findViewById(R.id.progress_alternative);
        img_close = view.findViewById(R.id.img_close);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void getDataFromBundle() {
        movie_id = Integer.parseInt(getArguments().getString("MOVIE_ID"));
        Log.d("MOVIE_ID", movie_id + "");

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
