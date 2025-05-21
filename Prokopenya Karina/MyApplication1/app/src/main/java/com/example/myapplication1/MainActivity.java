package com.example.myapplication1;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;




import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private TextView footerText;
    private Button btnShowChecked;
    private ArrayList<Product> products;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // Устанавливаем этот Toolbar как ActionBar

        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");

        listView = findViewById(R.id.listView);

        // Создаем заголовок и подвал
        View header = getLayoutInflater().inflate(R.layout.list_header, null);
        View footer = getLayoutInflater().inflate(R.layout.list_footer, null);
        footerText = footer.findViewById(R.id.footerText);
        btnShowChecked = footer.findViewById(R.id.btnShowChecked);
        Button btnSelectAll = footer.findViewById(R.id.btnSelectAll);

        listView.addHeaderView(header);
        listView.addFooterView(footer);

        // Заполняем список товарами
        products = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            int price = random.nextInt(491) + 10; // Цена от 10 до 500
            products.add(new Product(i, "Товар " + i, price, false));}

        adapter = new ProductAdapter(this, products, count -> runOnUiThread(this::updateFooterText));
        listView.setAdapter(adapter);

        // Обработчик кнопки "Показать выбранные товары"
        btnShowChecked.setOnClickListener(v -> {
            ArrayList<Product> checkedProducts = new ArrayList<>();
            for (Product p : products) {
                if (p.isChecked()) {
                    checkedProducts.add(p);
                }
            }

            // Передаем выбранные товары в другую активность
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("cart", checkedProducts);
            startActivity(intent);
        });

        btnSelectAll.setOnClickListener(v -> {
            for (Product p : products) {
                p.setChecked(true); // Устанавливаем флаг "выбран" для всех товаров
            }
            adapter.notifyDataSetChanged(); // Обновляем список товаров
            updateFooterText(); // Обновляем текст в футере
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info_lab) {
            // Открываем активность с информацией о лабораторной работе
            Intent labIntent = new Intent(MainActivity.this, LabInfoActivity.class);
            startActivity(labIntent);
            return true;
        } else if (item.getItemId() == R.id.action_info_student) {
            // Открываем активность с информацией о студенте
            Intent studentIntent = new Intent(MainActivity.this, StudentInfoActivity.class);
            startActivity(studentIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    // Метод для обновления текста в подвале
    private void updateFooterText() {
        int count = 0;
        for (Product p : products) {
            if (p.isChecked()) {
                count++;
            }
        }
        footerText.setText("Выбрано: " + count);
    }
}

