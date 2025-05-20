package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lab2.adapters.GoodsAdapter;
import com.example.lab2.interfaces.OnChangeListener;
import com.example.lab2.models.Good;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView listViewGoods;
    private TextView tvCount;
    private GoodsAdapter goodsAdapter;
    private List<Good> goodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewGoods = findViewById(R.id.listViewGoods);

        View header = getLayoutInflater().inflate(R.layout.header_mygoods, null);
        listViewGoods.addHeaderView(header);

        View footer = getLayoutInflater().inflate(R.layout.footer_mygoods, null);
        listViewGoods.addFooterView(footer);

        tvCount = footer.findViewById(R.id.tvCount);
        Button btnShowChecked = footer.findViewById(R.id.btnShowChecked);

        goodsList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            goodsList.add(new Good(i, "My good №" + i, i * 10.0, false));
        }

        goodsAdapter = new GoodsAdapter(this, goodsList);
        goodsAdapter.setOnChangeListener(new OnChangeListener() {
            @Override
            public void onCheckedChange() {
                updateCount();
            }
        });

        listViewGoods.setAdapter(goodsAdapter);

        btnShowChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Good> selectedGoods = new ArrayList<>();
                for (Good g : goodsList) {
                    if (g.isChecked()) {
                        selectedGoods.add(g);
                    }
                }
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putParcelableArrayListExtra("selectedGoods", selectedGoods);
                startActivity(intent);
            }
        });

        updateCount();
    }

    private void updateCount() {
        int count = 0;
        for (Good g : goodsList) {
            if (g.isChecked()) count++;
        }
        tvCount.setText("Count of goods: " + count);
    }
}
