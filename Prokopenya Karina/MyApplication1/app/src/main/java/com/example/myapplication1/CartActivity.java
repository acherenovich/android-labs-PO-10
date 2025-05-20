package com.example.myapplication1;

import android.os.Bundle;
import android.widget.ListView;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private ArrayList<Product> cartProducts;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        cartProducts = getIntent().getParcelableArrayListExtra("cart");

        // Проверяем, если cartProducts null, создаем пустой список
        if (cartProducts == null) {
            cartProducts = new ArrayList<>();
            Log.e("CartActivity", "No products passed, cart is empty.");
        }

        // Логируем полученные товары
        Log.d("CartActivity", "Received cart products: " + cartProducts.size());

        adapter = new ProductAdapter(this, cartProducts, count -> {});
        cartListView.setAdapter(adapter);
    }
}
