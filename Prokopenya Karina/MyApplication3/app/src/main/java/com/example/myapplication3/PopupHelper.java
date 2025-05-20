package com.example.myapplication3;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class PopupHelper {
    public static void showPopup(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Добавляем отступы (padding внутри)
        layout.setPadding(50, 50, 50, 50); // (left, top, right, bottom)

        // Настраиваем margin через LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,  // Ширина
                ViewGroup.LayoutParams.WRAP_CONTENT   // Высота
        );
        params.setMargins(50, 50, 50, 50); // (left, top, right, bottom)
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
