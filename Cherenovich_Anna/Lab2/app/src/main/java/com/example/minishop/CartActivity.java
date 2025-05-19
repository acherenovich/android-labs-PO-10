package com.example.minishop;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);

        // Получаем список выбранных товаров
        ArrayList<Product> checkedProducts = (ArrayList<Product>) getIntent().getSerializableExtra("checkedProducts");


        // Инициализируем адаптер
        productAdapter = new ProductAdapter(this, checkedProducts, null);

        cartListView.setAdapter(productAdapter);
    }
}
