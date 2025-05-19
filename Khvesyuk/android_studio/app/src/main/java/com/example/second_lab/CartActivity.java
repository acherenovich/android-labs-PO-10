package com.example.second_lab;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private TextView tvTotalPrice;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnBack = findViewById(R.id.btnBack);

        ArrayList<TechItem> selectedItems = (ArrayList<TechItem>) getIntent().getSerializableExtra("selectedItems");

        if (selectedItems == null || selectedItems.isEmpty()) {
            Toast.makeText(this, "Ошибка загрузки корзины!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        CartAdapter cartAdapter = new CartAdapter(this, selectedItems);
        cartListView.setAdapter(cartAdapter);

        double totalPrice = 0;
        for (TechItem item : selectedItems) {
            totalPrice += item.getPrice();
        }
        tvTotalPrice.setText(String.format("Общая стоимость: $%.2f", totalPrice));

        btnBack.setOnClickListener(v -> finish());
    }
}
