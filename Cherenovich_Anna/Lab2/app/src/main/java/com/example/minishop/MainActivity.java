package com.example.minishop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private TextView footerTextView;
    private Button showCheckedItemsButton;
    private ProductAdapter adapter;
    private List<Product> products;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        footerTextView = findViewById(R.id.footerTextView);
        showCheckedItemsButton = findViewById(R.id.showCheckedItemsButton);

        // Генерируем случайные товары
        products = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 20; i++) {
            int price = random.nextInt(491) + 10; // Цена от 10 до 500
            products.add(new Product(i,  i+ " Товар", price, false));
        }

        // Создаём адаптер с колбэком для обновления футера
        adapter = new ProductAdapter(this, products, this::updateFooterText);
        listView.setAdapter(adapter);

        // Кнопка "Показать выбранные товары"
        showCheckedItemsButton.setOnClickListener(v -> openCartActivity());
    }

    // Метод для обновления количества выбранных товаров в футере
    public void updateFooterText(int count) {
        runOnUiThread(() -> footerTextView.setText("Выбрано: " + count));
    }

    // Переход в корзину
    private void openCartActivity() {
        ArrayList<Product> checkedProducts = new ArrayList<>(adapter.getCheckedProducts());

        Intent intent = new Intent(this, CartActivity.class);
        intent.putParcelableArrayListExtra("checkedProducts", (ArrayList<? extends Parcelable>) checkedProducts);
        startActivity(intent);
    }
}
