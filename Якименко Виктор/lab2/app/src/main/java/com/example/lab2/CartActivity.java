package com.example.lab2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listViewCart;
    private CartAdapter cartAdapter;
    private List<Product> cartItems;
    private TextView totalPriceTextView; // Добавили
    private Button backButton; // Добавили

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        totalPriceTextView = findViewById(R.id.textViewTotalPrice); // Получаем ссылку
        backButton = findViewById(R.id.buttonBack); // Получаем ссылку
        cartItems = new ArrayList<>();

        ArrayList<Product> checkedProducts = getIntent().getParcelableArrayListExtra("checkedProducts");
        if (checkedProducts != null) {
            cartItems.addAll(checkedProducts);
        }

        // Передаем totalPriceTextView в конструктор адаптера
        cartAdapter = new CartAdapter(this, cartItems, totalPriceTextView);
        listViewCart.setAdapter(cartAdapter);

        // Обработчик нажатия на кнопку "Назад"
        backButton.setOnClickListener(v -> {
            finish(); // Закрываем текущую активность (CartActivity)
        });
    }
}