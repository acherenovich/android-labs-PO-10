package com.example.minishop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private OnCheckedChangeListener checkedChangeListener;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int count);
    }

    public ProductAdapter(Context context, List<Product> productList, OnCheckedChangeListener listener) {
        this.context = context;
        this.productList = productList;
        this.checkedChangeListener = listener;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        }

        Product product = productList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.productName);
        TextView priceTextView = convertView.findViewById(R.id.productPrice);
        CheckBox checkBox = convertView.findViewById(R.id.productCheckBox);

        nameTextView.setText(product.name);
        priceTextView.setText("Р. " + product.price);

        checkBox.setOnCheckedChangeListener(null); // Отключаем слушатель перед установкой состояния
        checkBox.setChecked(product.isChecked);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.isChecked = isChecked;
            updateSelectedCount();
        });

        return convertView;
    }

    public List<Product> getCheckedProducts() {
        List<Product> checkedProducts = new ArrayList<>();
        for (Product product : productList) {
            if (product.isChecked) {
                checkedProducts.add(product);
            }
        }
        return checkedProducts;
    }

    private void updateSelectedCount() {
        int selectedCount = 0;
        for (Product product : productList) {
            if (product.isChecked) {
                selectedCount++;
            }
        }
        if (checkedChangeListener != null) {
            checkedChangeListener.onCheckedChanged(selectedCount);
        }
    }
}
