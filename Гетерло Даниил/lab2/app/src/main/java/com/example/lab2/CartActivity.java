package com.example.lab2;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private List<Product> selectedProducts;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);

        selectedProducts = getIntent().getParcelableArrayListExtra("selectedProducts");

        if (selectedProducts == null) {
            finish();
            return;
        }

        adapter = new ProductAdapter(this, selectedProducts);
        cartListView.setAdapter(adapter);
    }
}

