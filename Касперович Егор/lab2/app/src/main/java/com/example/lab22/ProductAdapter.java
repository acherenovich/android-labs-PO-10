package com.example.lab22;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private OnCheckedChangeListener listener;

    public ProductAdapter(Context context, List<Product> productList, OnCheckedChangeListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }

        Product product = productList.get(position);

        TextView tvId = convertView.findViewById(R.id.tvId);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        CheckBox cbChecked = convertView.findViewById(R.id.cbChecked);

        tvId.setText("ID: " + product.getId());
        tvName.setText("Название: " + product.getName());
        tvPrice.setText("Цена: " + product.getPrice() + " руб.");

        // Скрыть чекбокс, если слушатель не передан (корзина)
        if (listener == null) {
            cbChecked.setVisibility(View.GONE);
        } else {
            cbChecked.setVisibility(View.VISIBLE);
            cbChecked.setChecked(product.isChecked());
            cbChecked.setOnCheckedChangeListener((buttonView, isChecked) -> {
                product.setChecked(isChecked);
                listener.onCheckedChanged(position, isChecked);
            });
        }

        return convertView;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int position, boolean isChecked);
    }
}