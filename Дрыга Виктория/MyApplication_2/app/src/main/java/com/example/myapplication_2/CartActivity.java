package com.example.myapplication_2;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private TextView cartSummary;
    private List<Product> selectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cart_listView);
        cartSummary = findViewById(R.id.cart_summary);

        selectedProducts = (List<Product>) getIntent().getSerializableExtra("selectedProducts");

        if (selectedProducts == null) {
            selectedProducts = new ArrayList<>();
        }

        int itemCount = selectedProducts.size();
        double totalPrice = 0;
        for (Product product : selectedProducts) {
            totalPrice += product.getPrice();
        }

        cartSummary.setText("There are: " + itemCount + " products\nTotal cost: $" + String.format("%.2f", totalPrice));

        CartAdapter adapter = new CartAdapter(this, selectedProducts);
        cartListView.setAdapter(adapter);
    }
}
