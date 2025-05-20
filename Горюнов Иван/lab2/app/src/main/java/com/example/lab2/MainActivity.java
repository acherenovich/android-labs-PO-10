package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductCheckedChangeListener {
    private ListView listView;
    private TextView selectedCountText;
    private Button showCheckedItems;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        selectedCountText = findViewById(R.id.selectedCountText);
        showCheckedItems = findViewById(R.id.showCheckedItems);

        View header = getLayoutInflater().inflate(R.layout.list_header, listView, false);
        View footer = getLayoutInflater().inflate(R.layout.list_footer, listView, false);
        listView.addHeaderView(header);
        listView.addFooterView(footer);

        populateProducts();
        adapter = new ProductAdapter(this, productList, this);
        listView.setAdapter(adapter);

        showCheckedItems.setOnClickListener(v -> {
            ArrayList<Product> selectedProducts = new ArrayList<>();
            for (Product p : productList) {
                if (p.isChecked()) selectedProducts.add(p);
            }
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("selectedProducts", selectedProducts);
            startActivity(intent);
        });
    }

    private void populateProducts() {
        productList.add(new Product("Ноутбук", 1200.99));
        productList.add(new Product("Смартфон", 799.49));
        productList.add(new Product("Наушники", 199.99));
        productList.add(new Product("Часы", 249.99));
    }

    @Override
    public void onCheckedChanged() {
        long count = productList.stream().filter(Product::isChecked).count();
        selectedCountText.setText("Выбрано: " + count);
    }
}
