package com.example.lab2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CheckedItemsObserver.OnChangeListener,
        View.OnClickListener{
    private LayoutInflater layoutInflater;
    private ListView listView;
    private ItemsAdapter adapter;
    private List<Item> itemList;
    private ArrayList<Item> checked_items_array = new ArrayList<Item>();
    private TextView footer_counter;

    private Button footer_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        itemList = new ArrayList<>();
        itemList.add(new Item(1, "Samsung Galaxy S25 Ultra 12GB256GB", 5599.00));
        itemList.add(new Item(2, "LG 65NANO80T6A", 2647.00));
        itemList.add(new Item(3, "Dreame Vacuum Cleaner wet and dry H12S", 1359.00));
        itemList.add(new Item(4, "Hyundai WMD9425", 5999.00));
        itemList.add(new Item(5, "Apple AirPods Max", 2859.00));
        itemList.add(new Item(6, "Samsung RF65A93T0SRWT", 11599.00));
        itemList.add(new Item(7, "MSI CreatorPro X18 HX A14VMG-415RU", 31826.00));

        adapter = new ItemsAdapter(this, itemList,this);
        listView.setAdapter(adapter);

        layoutInflater = LayoutInflater.from(this);
        View header = layoutInflater.inflate(R.layout.header, null);
        View footer = layoutInflater.inflate(R.layout.footer, null);
        footer_counter = footer.findViewById(R.id.cheked_items);
        Button labDataButton = header.findViewById(R.id.load_data_button);
        footer_button = footer.findViewById(R.id.button_change_activity);
        footer_button.setOnClickListener(this);
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        labDataButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Данные о лабораторной работе")
                    .setMessage("Сделал: Бородкин Д.В.ПО-10\n" + "1. Разработать приложение MiniShop, состоящее из двух Activity (см. рисунки 3.3, 3.4 в источнике)."+
                            "\n2. В первом Activity создать список ListView с Header и Footer."+
                            "\n3. В Footer разместить текстовое поле (TextView) для ввода количества ак-"+
                            "тивированных пользователем товаров, кнопку Show Checked Items для перехода в корзину товаров."+
                            "\n4. Реализовать кастомизированный список ListView с помощью собственно"+
                            "го адаптера, наследующего класс BaseAdapter."+
                            "\n5. В каждом пункте списка отобразить следующую информацию о товаре:"+
                            "идентификационный номер, название, стоимость, чекбокс для возможности выбора товара пользователем."+
                            "\n6. В текстовом поле (TextView) Footer списка динамически отображать об"+
                            "щее текущее количество активированных товаров."+
                            "\n7. При нажатии кнопки Show Checked Items реализовать переход во второе"+
                            "Activity с корзиной товаров."+
                            "\n8. Корзину товаров реализовать в виде нового кастомизированного списка с"+
                            "выбранными товарами."+
                            "\n9. Продемонстрировать работу приложения MiniShop на эмуляторе или ре"+
                            "альном устройстве.")
                    .create()
                    .show();

        });
    }
    @Override
    public void onClick(View view){
        checked_items_array = adapter.getCheckedItems();
        Collections.sort(checked_items_array);
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putParcelableArrayListExtra("MyList",  checked_items_array);
        startActivity(intent);
    }
    @Override
    public void onDataChanged(){
        int size = adapter.getCheckedItems().size();
        footer_counter.setText("Количество отмеченных товаров = " + size + "");
    }


}
