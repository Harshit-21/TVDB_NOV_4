package com.example.harshit.tvdb.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.harshit.tvdb.Activities.NonInternetorErrorActivity;

import java.io.File;
import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Harshit on 10/24/2017.
 */

public class AppUtil {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static void openNonInternetActivity(Context context, String tag) {
        Intent noInternetIntent = new Intent(context, NonInternetorErrorActivity.class);
        noInternetIntent.putExtra("TAG", tag);
        context.startActivity(noInternetIntent);

    }


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static String currencyFormatter(String amount) {
        double amount1 = Double.parseDouble(amount);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String moneyString = formatter.format(amount1);
        return moneyString;
    }


    public static String getFormattedTime(int time) {
        if (time <= 60) {
            return String.valueOf(time) + " min";
        } else {

            int hour = time / 60;
            // watever the hour came
            int count = hour * 60;
            int remaining_time = time - count;
            return hour + " hour and " + remaining_time + " min";
        }
    }


    public static boolean isFileExists(Context context, String filename) {
        String currentFilePath = context.getFilesDir() + "/" + filename;
        File currentfile = new File(currentFilePath);

        if (currentfile.exists()) {
            return true;
        }
        return false;
    }


    public static String getFormattedDate(String release_date) {
        //2017-10-06
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(release_date);
            String release = df.format(date);
            return release;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int heightOfScreen(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static int widthOfScreen(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
                                                          int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }

    public static void setActionBar(Context context) {
        try {
            AppCompatActivity activity = (AppCompatActivity) context;
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
