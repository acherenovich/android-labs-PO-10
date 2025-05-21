package com.example.lab4.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class PopupHelper {
    public static void showPopup(Context context) {
        if (!SettingHelper.shouldShowPopup(context)) {
            return;
        }

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        CheckBox checkBox = new CheckBox(context);
        checkBox.setText("Don't show again");

        layout.addView(checkBox);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Instruction")
                .setMessage("Welcome!\n\nHere you can download, read and delete files.")
                .setView(layout)
                .setPositiveButton("ОК", (dlg, which) -> {
                    if (checkBox.isChecked()) {
                        SettingHelper.setShowPopup(context, false);
                    }
                })
                .create();

        dialog.show();
    }
}