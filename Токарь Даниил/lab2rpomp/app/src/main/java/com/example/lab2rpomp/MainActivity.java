package com.example.lab2rpomp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnQuantityChangeListener { // Изменили интерфейс

    private ListView productListView;
    private TextView checkedItemsCountTextView; // Текст "Выбрано товаров" теперь будет показывать и сумму
    private Button showCartButton;
    private ProductAdapter productAdapter;
    private List<Product> products;
    private double totalPrice = 0.0; // Переменная для хранения общей стоимости

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productListView = findViewById(R.id.productList);
        checkedItemsCountTextView = findViewById(R.id.checkedItemsCountTextView);
        showCartButton = findViewById(R.id.showCartButton);

        // Создаем список товаров (заглушка - замени на свои данные)
        products = new ArrayList<>();
        products.add(new Product(1, "F/A-18C Early", 400.0, R.drawable.f18pack)); // Используем R.drawable.apple (пример)
        products.add(new Product(2, "Ariete Certezza", 350.0, R.drawable.arietepack)); // R.drawable.banana (пример)
        products.add(new Product(3, "БМД-4М", 300.0, R.drawable.bmdpack)); // R.drawable.orange (пример)
        products.add(new Product(4, "Marder CLOVIS", 230.0, R.drawable.marderpack)); // R.drawable.pear (пример)
        products.add(new Product(5, "Золотые орлы, 5000 ед.", 60.0, R.drawable.gepack)); // R.drawable.grapes (пример)
        products.add(new Product(6, "Премиум аккаунт, 60 дн.", 120.0, R.drawable.premaccpack));

        // Создаем адаптер, передавая контекст, список товаров и интерфейс обратного вызова (this - MainActivity)
        productAdapter = new ProductAdapter(this, products, this); // Передаем this как OnQuantityChangeListener

//        LayoutInflater inflater = LayoutInflater.from(this);
//        View headerView = inflater.inflate(R.layout.header_layout, productListView, false);
//        View footerView = inflater.inflate(R.layout.footer_layout, productListView, false);
//        productListView.addHeaderView(headerView);
//        productListView.addFooterView(footerView);

        productListView.setAdapter(productAdapter);

        updateTotalPrice(); // Инициализируем сумму

        showCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            List<Product> selectedProducts = getSelectedProducts();
            intent.putExtra("selectedProducts", new ArrayList<>(selectedProducts));
            intent.putExtra("totalPrice", totalPrice); // Передаем общую стоимость в CartActivity
            startActivity(intent);
        });
    }

    @Override
    public void onQuantityChanged() { // Реализация метода OnQuantityChangeListener
        updateTotalPrice(); // Обновляем общую стоимость при изменении количества
    }

    private void updateTotalPrice() {
        totalPrice = 0.0;
        int totalQuantity = 0;
        for (Product product : products) {
            totalPrice += product.getPrice() * product.getQuantity();
            totalQuantity += product.getQuantity();
        }
        checkedItemsCountTextView.setText(String.format("Выбрано товаров: %d, на сумму: %.2f руб.", totalQuantity, totalPrice)); // Отображаем количество и сумму
    }

    private List<Product> getSelectedProducts() {
        List<Product> selectedProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() > 0) { // Выбираем товары с количеством > 0
                selectedProducts.add(product);
            }
        }
        return selectedProducts;
    }
}