package com.example.harshit.tvdb.Activities;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harshit.tvdb.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NonInternetorErrorActivity extends AppCompatActivity {
    private String tag = "";
    private TextView tv_message;
    private ImageView img_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_internet);
        initViews();
        tag = getIntent().getStringExtra("TAG");
        if (!tag.equalsIgnoreCase(getResources().getString(R.string.no_internet))) {
            tv_message.setText(getResources().getString(R.string.something_went_wrong));
            img_message.setBackgroundResource(R.drawable.something_went_wrong);
        }
    }

    private void initViews() {
        tv_message = (TextView) findViewById(R.id.tv_message);
        img_message = (ImageView) findViewById(R.id.img_message);
    }


}
