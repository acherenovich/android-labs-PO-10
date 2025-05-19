package com.example.lab4rpomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textViewNetworkStatus;
    private Button buttonHttpGet;
    private Button buttonSocketClient;
    private Button buttonFileDownload;
    private Button buttonResetInstruction;

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_SHOW_INSTRUCTION = "showInstruction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewNetworkStatus = findViewById(R.id.textViewNetworkStatus);
        buttonHttpGet = findViewById(R.id.buttonHttpGet);
        buttonSocketClient = findViewById(R.id.buttonSocketClient);
        buttonFileDownload = findViewById(R.id.buttonFileDownload);
        buttonResetInstruction = findViewById(R.id.buttonResetInstruction);

        buttonResetInstruction.setOnClickListener(v -> {
            resetInstructionPopupSetting(); // Вызываем метод для сброса настройки
        });
        // Проверка наличия интернета (Задание 1)
        if (isNetworkAvailable()) {
            textViewNetworkStatus.setText("Интернет доступен");
            Toast.makeText(this, "Интернет есть!", Toast.LENGTH_SHORT).show();
        } else {
            textViewNetworkStatus.setText("Интернет отсутствует");
            Toast.makeText(this, "Нет интернета!", Toast.LENGTH_SHORT).show();
        }

        // Обработчики кнопок
        buttonHttpGet.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HttpGetActivity.class)));
        buttonSocketClient.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SocketClientActivity.class)));
        buttonFileDownload.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FileDownloadActivity.class)));

        // Показ PopupWindow с инструкцией (Задание 4) - отложенный показ после отрисовки Activity
        if (shouldShowInstructionPopup()) {
            final View rootView = findViewById(android.R.id.content); // Получаем корневой View
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Этот код будет выполнен, когда rootView будет отрисован на экране

                    // Отключаем слушатель, чтобы он не вызывался много раз
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    showInstructionPopup(); // Теперь показываем PopupWindow
                }
            });
        }
    }

    // Метод для проверки доступности сети (как в Задании 1)
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    // Проверка, нужно ли показывать инструкцию
    private boolean shouldShowInstructionPopup() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_SHOW_INSTRUCTION, true); // По умолчанию показывать
    }

    // Показ PopupWindow с инструкцией
    private void showInstructionPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_instruction, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true); // true чтобы можно было закрыть по тапу вне окна

        CheckBox checkBoxDontShowAgain = popupView.findViewById(R.id.checkBoxDontShowAgain);
        Button buttonOk = popupView.findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(v -> {
            if (checkBoxDontShowAgain.isChecked()) {
                // Сохраняем настройку "не показывать больше"
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(KEY_SHOW_INSTRUCTION, false);
                editor.apply();
            }
            popupWindow.dismiss();
        });

        // Показываем PopupWindow в центре экрана, используя корневой View как якорь
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0); // <---- Измененная строка

    }
    private void resetInstructionPopupSetting() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_SHOW_INSTRUCTION, true); // Устанавливаем значение "показывать инструкцию" обратно в true
        editor.apply(); // Применяем изменения

        Toast.makeText(this, "Инструкция будет показана при следующем запуске приложения", Toast.LENGTH_SHORT).show(); // Сообщаем пользователю
    }
}