package com.example.harshit.tvdb.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class Preference {


    private final static String PREF_NAME = "TVDB_PREF";

    public static final String is_User_Info_saved = "IS_USER_VALUE_SAVED";


    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String readString(Context context, final String key,
                                    String defaultValue) {
        SharedPreferences pref = getSharedPreference(context);
        return pref.getString(key, defaultValue);

    }


    public static void writeString(Context context, final String key, final String value) {
        SharedPreferences settings = getSharedPreference(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void writeBoolean(Context context, final String key, final boolean value) {
        SharedPreferences settings = getSharedPreference(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean readBoolean(Context context, final String key, final boolean defaultValue) {
        SharedPreferences settings = getSharedPreference(context);
        return settings.getBoolean(key, defaultValue);
    }

    public static int readInt(Context context, final String key, final int defaultValue) {
        SharedPreferences settings = getSharedPreference(context);
        return settings.getInt(key, defaultValue);

    }

    public static void writeInt(Context _context, final String key, final int value) {
        SharedPreferences settings = getSharedPreference(_context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public static long readLong(Context _context, final String key, final long defaultValue) {
        SharedPreferences settings = getSharedPreference(_context);
        return settings.getLong(key, defaultValue);

    }

    public static void writeLong(Context _context, final String key, final long value) {
        SharedPreferences settings = getSharedPreference(_context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }


}
