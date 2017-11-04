package com.example.harshit.tvdb.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.harshit.tvdb.Activities.NonInternetorErrorActivity;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
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
        double amount1=Double.parseDouble(amount);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String moneyString = formatter.format(amount1);
        return moneyString;
    }


    public static String getFormattedTime(int time) {
        if (time <= 60) {
            return String.valueOf(time)+" min";
        } else {

            int hour = time / 60;
            // watever the hour came
            int count = hour * 60;
            int remaining_time = time - count;
            return hour + " hour and " + remaining_time+" min";
        }
    }


}