package com.example.myapplication1;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Handler;


class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Product> products;
    private UpdateSelectedListener listener;

    public interface UpdateSelectedListener {
        void onUpdateSelectedCount(int count);
    }

    public ProductAdapter(Context context, ArrayList<Product> products, UpdateSelectedListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false);
        }

        Product product = products.get(position);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtPrice = convertView.findViewById(R.id.txtPrice);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        txtName.setText(product.getName());
        txtPrice.setText("Цена: " + product.getPrice() + " руб.");
        checkBox.setChecked(product.isChecked());

        // Слушатель для клика по чекбоксу
        checkBox.setOnClickListener(v -> {
            boolean isChecked = !product.isChecked();  // Инвертируем состояние
            product.setChecked(isChecked);
            checkBox.setChecked(isChecked);  // Обновляем чекбокс для UI
            updateSelectedCount();  // Обновляем количество выбранных товаров
        });

        return convertView;
    }

    // Метод для обновления количества выбранных товаров
    private void updateSelectedCount() {
        int count = 0;
        for (Product p : products) {
            if (p.isChecked()) {
                count++;
            }
        }
        listener.onUpdateSelectedCount(count);
    }
}

