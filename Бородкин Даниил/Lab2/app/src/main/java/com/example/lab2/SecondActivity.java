package com.example.lab2;

import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        listView = findViewById(R.id.list_view_cart);

        // Получаем список из Intent
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            itemList = arguments.getParcelableArrayList("MyList");
            if (itemList != null) {
                // Создаем и устанавливаем адаптер
                ItemCartAdapter adapter = new ItemCartAdapter(this, itemList);
                listView.setAdapter(adapter);
            }
        }
        TextView tv_count = findViewById(R.id.tv_count);
        tv_count.setText("Количество товаров в корзине = " + listView.getCount());

    }
}
