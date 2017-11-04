package com.example.harshit.tvdb.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harshit.tvdb.Application.MyApplication;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvDetailActivity extends AppCompatActivity {

    private Bean_MovieDetails bean_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);

    }



}
