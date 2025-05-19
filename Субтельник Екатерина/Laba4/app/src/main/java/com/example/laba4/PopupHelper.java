package com.example.laba4;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class PopupHelper {
    public static void showPopup(Context context) {
        if (!SettingsManager.shouldShowPopup(context)) {
            return;
        }

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        CheckBox checkBox = new CheckBox(context);
        checkBox.setText("Do not show anymore");

        layout.addView(checkBox);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Instruction")
                .setMessage("Hello\n\nHere you can download files")
                .setView(layout)
                .setPositiveButton("ОК", (dlg, which) -> {
                    if (checkBox.isChecked()) {
                        SettingsManager.setShowPopup(context, false);
                    }
                })
                .create();

        dialog.show();
    }
}
