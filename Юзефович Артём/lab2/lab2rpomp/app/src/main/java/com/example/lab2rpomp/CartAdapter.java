package com.example.lab2rpomp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView; // Импорт ImageView
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<Product> cartItems;

    public CartAdapter(Context context, List<Product> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
            holder = new ViewHolder();
            holder.productImageView = convertView.findViewById(R.id.cartProductImageView); // Находим ImageView в корзине
            holder.productNameTextView = convertView.findViewById(R.id.cartProductNameTextView);
            holder.productPriceTextView = convertView.findViewById(R.id.cartProductPriceTextView);
            holder.productQuantityTextView = convertView.findViewById(R.id.cartProductQuantityTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = cartItems.get(position);
        // Устанавливаем изображение в корзине
        holder.productImageView.setImageResource(product.getImageResourceId());
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText(String.format("%.2f", product.getPrice()) + " руб.");
        holder.productQuantityTextView.setText("x" + product.getQuantity());

        return convertView;
    }

    static class ViewHolder {
        ImageView productImageView; // Добавлено поле для ImageView в ViewHolder корзины
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productQuantityTextView;
    }
}
