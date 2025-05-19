package com.example.fourth_lab;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class PopupHelper {
    public static void showPopup(Context context) {
        // Проверяем, нужно ли показывать Popup
        if (!Settings.shouldShowPopup(context)) {
            return;
        }

        // Создаем макет для всплывающего окна
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        CheckBox checkBox = new CheckBox(context);
        checkBox.setText("Больше не показывать");

        // Добавляем чекбокс в layout
        layout.addView(checkBox);

        // Создаем и показываем диалог с инструкцией
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Инструкция")
                .setMessage("Здесь вы можете скачать файлы и выполнять другие операции.")
                .setView(layout)
                .setPositiveButton("ОК", (dlg, which) -> {
                    // Если чекбокс выбран, скрываем этот popup в будущем
                    if (checkBox.isChecked()) {
                        Settings.setShowPopup(context, false);
                    }
                })
                .create();

        dialog.show();
    }
}
