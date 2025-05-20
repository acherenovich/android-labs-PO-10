package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.lab2.adapters.CartAdapter;
import com.example.lab2.models.Good;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private ListView listViewCart;
    private CartAdapter cartAdapter;
    private ArrayList<Good> selectedGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        listViewCart = findViewById(R.id.listViewCart);

        selectedGoods = getIntent().getParcelableArrayListExtra("selectedGoods");

        cartAdapter = new CartAdapter(this, selectedGoods);
        listViewCart.setAdapter(cartAdapter);
    }
}
