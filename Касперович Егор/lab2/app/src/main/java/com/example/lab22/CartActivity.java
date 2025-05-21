package com.example.lab22;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ListView listViewCart;
    private TextView tvTotalAmount; // TextView для отображения итоговой суммы
    private List<Product> selectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        tvTotalAmount = findViewById(R.id.tvTotalAmount); // Инициализация TextView

        // Получение выбранных товаров из Intent
        selectedProducts = getIntent().getParcelableArrayListExtra("selectedProducts");

        // Настройка адаптера для корзины
        ProductAdapter cartAdapter = new ProductAdapter(this, selectedProducts, null);
        listViewCart.setAdapter(cartAdapter);

        // Вычисление и отображение итоговой суммы
        double totalAmount = calculateTotalAmount(selectedProducts);
        tvTotalAmount.setText("Итоговая сумма: " + totalAmount + " руб.");
    }

    // Метод для вычисления итоговой суммы
    private double calculateTotalAmount(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
}