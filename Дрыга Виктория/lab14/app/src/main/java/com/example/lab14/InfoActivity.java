package com.example.lab14;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        TextView infoTextView = findViewById(R.id.info_text);

        String text = "Лабораторная работа № 14. Работа с файловой системой. Разрешения.<br/>" +
                "<ul>" +
                "<li>Создать приложение, обеспечивающее выбор файла во внешнем хранилище с возможностью дальнейшей его обработки в зависимости от расширения: </li>" +
                "<li>Загрузить заранее набор медиафайлов (изображения, аудио, видео) для тестирования.</li>" +
                "<li>Добавить необходимые элементы интерфейса для реализации всех функций, перечисленных в задании. Для реализации различных функций можно использовать как дополнительные разметки, так и дополнительные активности. </li>" +
                "</ul>";

        infoTextView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
    }
}
