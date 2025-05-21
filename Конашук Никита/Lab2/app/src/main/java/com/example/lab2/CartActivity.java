package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ListView listViewCart;
    private TextView txtTotalPrice;
    private ProductAdapter adapter;
    private ArrayList<Product> selectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);

        selectedProducts = (ArrayList<Product>) getIntent().getSerializableExtra("selectedProducts");

        if (selectedProducts != null) {
            // Передаем в адаптер callback для обновления суммы
            adapter = new ProductAdapter(this, selectedProducts, this::updateTotalPrice);
            listViewCart.setAdapter(adapter);
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        int totalPrice = 0;
        for (Product p : selectedProducts) {
            if (p.isChecked()) { // Учитываем только выбранные товары
                totalPrice += p.getPrice();
            }
        }
        txtTotalPrice.setText("Общая стоимость: " + totalPrice + " ₽");
    }

    @Override
    public void onBackPressed() {
        // Отправляем обновленный список товаров обратно в MainActivity
        Intent intent = new Intent();
        intent.putExtra("updatedProducts", selectedProducts);
        setResult(RESULT_OK, intent);
        finish();
    }
}
