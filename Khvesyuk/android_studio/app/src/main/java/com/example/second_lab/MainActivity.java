package com.example.second_lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private TextView tvSelectedCount;
    private Button btnShowChecked;
    private List<TechItem> techItems;
    private TechAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        tvSelectedCount = findViewById(R.id.tvSelectedCount);
        btnShowChecked = findViewById(R.id.btnShowChecked);

        techItems = new ArrayList<>();
        techItems.add(new TechItem(1, "Ноутбук", 1200.0));
        techItems.add(new TechItem(2, "Смартфон", 800.0));
        techItems.add(new TechItem(3, "Наушники", 150.0));
        techItems.add(new TechItem(4, "Клавиатура", 100.0));
        techItems.add(new TechItem(5, "Мышь", 50.0));

        adapter = new TechAdapter(this, techItems, tvSelectedCount);
        listView.setAdapter(adapter);

        btnShowChecked.setOnClickListener(v -> {
            ArrayList<TechItem> selectedItems = new ArrayList<>();
            for (TechItem item : techItems) {
                if (item.isChecked()) {
                    selectedItems.add(item);
                }
            }

            if (selectedItems.isEmpty()) {
                Toast.makeText(MainActivity.this, "Выберите хотя бы один товар!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra("selectedItems", selectedItems);
            startActivity(intent);
        });
    }
}
