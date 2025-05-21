package com.example.laba4;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_SHOW_POPUP = "showPopup";

    public static boolean shouldShowPopup(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_SHOW_POPUP, true);
    }

    public static void setShowPopup(Context context, boolean show) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_SHOW_POPUP, show).apply();
    }
}
