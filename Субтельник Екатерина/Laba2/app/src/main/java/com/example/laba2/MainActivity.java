package com.example.laba2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private TextView selectedCountTextView;
    private Button showCheckedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        productList = generateProducts();

        View header = LayoutInflater.from(this).inflate(R.layout.list_header, listView, false);
        listView.addHeaderView(header);

        View footer = LayoutInflater.from(this).inflate(R.layout.list_footer, listView, false);
        selectedCountTextView = footer.findViewById(R.id.selectedCount);
        showCheckedButton = footer.findViewById(R.id.showCheckedButton);
        listView.addFooterView(footer);

        adapter = new ProductAdapter(this, productList, this::updateSelectedCount);
        listView.setAdapter(adapter);

        showCheckedButton.setOnClickListener(v -> openCartActivity());
    }

    private List<Product> generateProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            products.add(new Product(i, "Product " + i, (10 + i * 2)));
        }
        return products;
    }

    private void updateSelectedCount() {
        long count = productList.stream().filter(Product::isChecked).count();
        selectedCountTextView.setText("Selected products: " + count);
    }

    private void openCartActivity() {
        ArrayList<Product> selectedProducts = new ArrayList<>();
        for (Product product : productList) {
            if (product.isChecked()) {
                selectedProducts.add(product);
            }
        }

        Intent intent = new Intent(this, CartActivity.class);
        intent.putParcelableArrayListExtra("selectedProducts", selectedProducts);
        startActivity(intent);
    }
}
