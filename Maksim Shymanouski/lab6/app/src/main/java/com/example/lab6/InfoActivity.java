package com.example.lab6;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView infoTextView = findViewById(R.id.info_text);

        String text = "Лабораторная работа 16 (Работа 6 из УМК) - Shymanouski.<br/>" +
                "Принципы работы с жестами, вводимыми пользователями.<br/><br/>" +
                "<b>Бонусы (то, что способствует оценке выше 4):</b><br/>" +
                "<ul>" +
                "<li>Разработка собственного СВЯЗНОГО набора жестов.</li>" +
                "<li>Представление в приложении дополнительной справочной информации (текст о назначении приложения).</li>" +
                "<li>Вывод различных информационных сообщений после соответствующих жестов ПОЛЬЗОВАТЕЛЯ. Подготовка сценария управления приложением с помощью жестов пользователя.</li>" +
                "<li>Возможность навигации по приложению с помощью набора жестов пользователя.</li>" +
                "</ul>";

        infoTextView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
    }
}
