package com.example.lab2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;
    private Runnable updateSelectedCountCallback;

    // Новый конструктор с дополнительным параметром
    public ProductAdapter(Context context, List<Product> products, Runnable updateSelectedCountCallback) {
        this.context = context;
        this.products = products;
        this.updateSelectedCountCallback = updateSelectedCountCallback;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }

        Product product = products.get(position);

        // Получаем ссылки на элементы UI по ID
        TextView txtId = convertView.findViewById(R.id.productId);
        TextView txtName = convertView.findViewById(R.id.productName);
        TextView txtPrice = convertView.findViewById(R.id.productPrice);
        CheckBox checkBox = convertView.findViewById(R.id.productCheckbox);

        // Заполняем данные товара
        txtId.setText(String.valueOf(product.getId()));
        txtName.setText(product.getName());
        txtPrice.setText(String.format("₽%d", product.getPrice()));
        checkBox.setChecked(product.isChecked());

        // Обрабатываем изменения состояния чекбокса
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setChecked(isChecked);
            if (updateSelectedCountCallback != null) {
                updateSelectedCountCallback.run(); // Обновляем количество выбранных товаров
            }
        });

        return convertView;
    }
}


