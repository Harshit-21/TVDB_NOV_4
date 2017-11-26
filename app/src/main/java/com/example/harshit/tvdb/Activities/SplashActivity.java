package com.example.harshit.tvdb.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppUtil;

public class SplashActivity extends AppCompatActivity {
    private TextView tv_title;


    private static final long SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();

        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                startActivity(i,  ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void initViews() {
        tv_title = findViewById(R.id.tv_title);
        int height = AppUtil.heightOfScreen(this);
        int width = AppUtil.widthOfScreen(this);

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(tv_title, View.TRANSLATION_Y, 0, height / 2);
        objectAnimator2.setDuration(2500);
        objectAnimator2.setInterpolator(new BounceInterpolator());
//        objectAnimator2.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(tv_title, View.ALPHA, .7f);
        objectAnimator1.setDuration(2000);
        objectAnimator1.setRepeatCount(ValueAnimator.INFINITE);

        objectAnimator1.setInterpolator(new AccelerateInterpolator());
//        objectAnimator1.start();


        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playSequentially(objectAnimator2,objectAnimator1);
        animatorSet.start();



//        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(tv_title, "translationX", 0f, width / 2f);
//        objectAnimator1.setDuration(1000);
//        objectAnimator1.setStartDelay(3000);
//        objectAnimator1.setInterpolator(new BounceInterpolator());
//        objectAnimator1.start();
//
//        AnimatorSet animatorSet=new AnimatorSet();
//        animatorSet.play(objectAnimator2).before(objectAnimator1);
//        animatorSet.start();



    }


}
