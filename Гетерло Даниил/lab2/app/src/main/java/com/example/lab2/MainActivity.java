package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ListView productListView;
    private TextView selectedCountTextView;
    private List<Product> productList;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productListView = findViewById(R.id.productListView);

        // Создание Header
        View headerView = getLayoutInflater().inflate(R.layout.list_header, productListView, false);
        productListView.addHeaderView(headerView);

        // Создание Footer
        View footerView = getLayoutInflater().inflate(R.layout.list_footer, productListView, false);
        selectedCountTextView = footerView.findViewById(R.id.selectedCountTextView);
        Button showCheckedButton = footerView.findViewById(R.id.showCheckedButton);
        showCheckedButton.setOnClickListener(v -> showCheckedItems());
        productListView.addFooterView(footerView);

        // Заполнение списка фруктами
        productList = new ArrayList<>();
        productList.add(new Product(1, " Яблоко ", 1.5, R.drawable.apple));
        productList.add(new Product(2, " Банан ", 5.1, R.drawable.banana));
        productList.add(new Product(3, " Апельсин ", 5.0, R.drawable.orange));
        productList.add(new Product(4, " Груша ", 5.4, R.drawable.pear));
        productList.add(new Product(5, " Клубника ", 9.7, R.drawable.strawberry));
        productList.add(new Product(6, " Арбуз ", 5.6, R.drawable.watermelon));
        productList.add(new Product(7, " Виноград ", 20.5, R.drawable.grape));
        productList.add(new Product(5, " Авокадо ", 14.4, R.drawable.avocado));
        productList.add(new Product(5, " Персик ", 6.4, R.drawable.peach));

        adapter = new ProductAdapter(this, productList);
        productListView.setAdapter(adapter);
    }

    public void updateFooterCount(long count) {
        selectedCountTextView.setText("Выбрано товаров: " + count);
    }

    private void showCheckedItems() {
        ArrayList<Product> selectedProducts = new ArrayList<>();
        for (Product product : productList) {
            if (product.isChecked()) {
                selectedProducts.add(product);
            }
        }

        if (selectedProducts.isEmpty()) {
            Toast.makeText(this, "Нет выбранных товаров", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, CartActivity.class);
        intent.putParcelableArrayListExtra("selectedProducts", selectedProducts);
        startActivity(intent);
    }
}
