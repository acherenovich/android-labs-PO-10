package com.example.lab4;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;

public class PopupHelper {
    public static void showPopup(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("app_prefs", Activity.MODE_PRIVATE);
        if (prefs.getBoolean("dont_show_again", false)) return;

        View popupView = LayoutInflater.from(activity).inflate(R.layout.popup_window, null);
        PopupWindow popupWindow = new PopupWindow(popupView, 800, 600, true);
        popupWindow.showAtLocation(activity.findViewById(android.R.id.content), 0, 0, 0);

        CheckBox checkBox = popupView.findViewById(R.id.checkbox);
        Button okButton = popupView.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                prefs.edit().putBoolean("dont_show_again", true).apply();
            }
            popupWindow.dismiss();
        });
    }
}

