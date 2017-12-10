package com.example.harshit.tvdb.Utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.harshit.tvdb.Activities.NonInternetorErrorActivity;
import com.example.harshit.tvdb.Activities.SplashActivity;

import java.io.File;
import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Harshit on 10/24/2017.
 */

public class AppUtil {

    public static void hideKeypad(Activity activity)
    {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


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
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            context.startActivity(noInternetIntent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());

        } else {
            context.startActivity(noInternetIntent);
        }
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


    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }



    public static boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
