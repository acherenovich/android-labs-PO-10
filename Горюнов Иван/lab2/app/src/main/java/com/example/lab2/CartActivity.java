package com.example.lab2;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.stream.Collectors;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private List<Product> selectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        selectedProducts = getIntent().getParcelableArrayListExtra("selectedProducts");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                selectedProducts.stream().map(Product::getName).collect(Collectors.toList()));
        cartListView.setAdapter(adapter);
    }
}
