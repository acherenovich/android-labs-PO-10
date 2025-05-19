package com.example.lab2rpomp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView; // Импорт ImageView
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;
    private OnQuantityChangeListener quantityChangeListener;

    public ProductAdapter(Context context, List<Product> productList, OnQuantityChangeListener listener) {
        this.context = context;
        this.productList = productList;
        this.quantityChangeListener = listener;
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder();
            holder.productImageView = convertView.findViewById(R.id.productImageView); // Находим ImageView
            holder.productIdTextView = convertView.findViewById(R.id.productIdTextView);
            holder.productNameTextView = convertView.findViewById(R.id.productNameTextView);
            holder.productPriceTextView = convertView.findViewById(R.id.productPriceTextView);
            holder.quantityTextView = convertView.findViewById(R.id.quantityTextView);
            holder.quantityMinusButton = convertView.findViewById(R.id.quantityMinusButton);
            holder.quantityPlusButton = convertView.findViewById(R.id.quantityPlusButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);

        // Устанавливаем изображение
        holder.productImageView.setImageResource(product.getImageResourceId()); // Устанавливаем изображение из ресурса

        holder.productIdTextView.setText("ID: " + product.getId());
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText(String.format("%.2f", product.getPrice()) + " руб.");
        holder.quantityTextView.setText(String.valueOf(product.getQuantity()));

        holder.quantityMinusButton.setOnClickListener(v -> {
            int currentQuantity = product.getQuantity();
            if (currentQuantity > 0) {
                product.setQuantity(currentQuantity - 1);
                holder.quantityTextView.setText(String.valueOf(product.getQuantity()));
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged();
                }
            }
        });

        holder.quantityPlusButton.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            holder.quantityTextView.setText(String.valueOf(product.getQuantity()));
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView productImageView; // Добавлено в ViewHolder
        TextView productIdTextView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView quantityTextView;
        Button quantityMinusButton;
        Button quantityPlusButton;
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }
}