package com.example.harshit.tvdb.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppUtil;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AppUtil.setActionBar(this);

    }
}
