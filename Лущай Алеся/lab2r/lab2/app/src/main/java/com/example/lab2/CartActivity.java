package com.example.lab2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private ArrayList<Product> selectedProducts;
    private CartAdapter cartAdapter;

    private TextView totalCount, totalPrice;
    private View view_header;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.list);

        layoutInflater = LayoutInflater.from(this);
        view_header = layoutInflater.inflate(R.layout.cart_header, null);
        cartListView.addHeaderView(view_header);

        totalCount = findViewById(R.id.totalCount);
        totalPrice = findViewById(R.id.totalPrice);

        // Получаем переданные товары из Intent
        selectedProducts = getIntent().getParcelableArrayListExtra("selected_products");

        totalCount.setText("Товары: " + selectedProducts.size());

        Stream<Product> productStream = selectedProducts.stream();
        double sum = selectedProducts.stream().mapToDouble(p -> p.getPrice()).sum();
        totalPrice.setText("Итого: " + String.format(Locale.getDefault(), "%.2f", sum) + " р");

        // получаем элемент ListView
        cartListView = findViewById(R.id.list);

        cartAdapter = new CartAdapter(this, R.layout.cart_item, selectedProducts);
        cartListView.setAdapter(cartAdapter);

    }

}