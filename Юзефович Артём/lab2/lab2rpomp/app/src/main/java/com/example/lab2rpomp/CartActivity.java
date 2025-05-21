package com.example.lab2rpomp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView cartListView;
    private CartAdapter cartAdapter;
    private List<Product> selectedProducts;
    private TextView cartTotalPriceTextView; // TextView для общей стоимости корзины
    private double totalPrice = 0.0; // Переменная для общей стоимости

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartItemList);
        cartTotalPriceTextView = findViewById(R.id.cartTotalPriceTextView); // Находим TextView для общей стоимости

        // Получаем список выбранных товаров из Intent
        selectedProducts = (List<Product>) getIntent().getSerializableExtra("selectedProducts");
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0); // Получаем общую стоимость из Intent

        if (selectedProducts == null) {
            selectedProducts = java.util.Collections.emptyList();
        }

        cartAdapter = new CartAdapter(this, selectedProducts);
        cartListView.setAdapter(cartAdapter);

        updateCartTotalPrice(); // Рассчитываем и отображаем общую стоимость корзины
    }

    private void updateCartTotalPrice() {
        cartTotalPriceTextView.setText(String.format("Общая стоимость: %.2f руб.", totalPrice)); // Отображаем общую стоимость
    }
}
