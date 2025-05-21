package com.example.lab_4;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class PopupManager {
    public static void showPopup(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.setPadding(50, 50, 50, 50);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(50, 50, 50, 50);
        layout.setLayoutParams(params);

        CheckBox checkBox = new CheckBox(context);
        checkBox.setText("Больше не показывать");

        layout.addView(checkBox);

        if (SettingsManager.shouldShowPopup(context)) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Инструкция")
                    .setMessage("Загрузка выпусков журнала научно-технический вестник")
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
}
