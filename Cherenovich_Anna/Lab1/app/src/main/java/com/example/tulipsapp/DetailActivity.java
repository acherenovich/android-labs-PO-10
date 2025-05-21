package com.example.tulipsapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Получаем переданный объект Tulip через Parcelable
        Tulip tulip = getIntent().getParcelableExtra("tulip");

        // Инициализируем элементы UI
        TextView name = findViewById(R.id.tulipName);
        TextView color = findViewById(R.id.tulipColor);
        TextView height = findViewById(R.id.tulipHeight);  // Для высоты тюльпана
        ImageView image = findViewById(R.id.tulipImage);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Устанавливаем Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Заполняем данными из объекта Tulip
        if (tulip != null) {
            name.setText(tulip.getName());
            color.setText(tulip.getColor());
            height.setText("Высота: " + tulip.getHeight());  // Отображаем высоту тюльпана
            Picasso.get().load(tulip.getImageUrl())
                    .error(R.drawable.error) // Пример изображения для ошибки
                    .into(image);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Переход назад при нажатии кнопки "Назад"
        return true;
    }
}
