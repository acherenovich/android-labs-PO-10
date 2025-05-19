package com.example.laba2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Product> cartProducts;

    public CartAdapter(Context context, List<Product> cartProducts) {
        this.context = context;
        this.cartProducts = cartProducts;
    }

    @Override
    public int getCount() {
        return cartProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return cartProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        Product product = cartProducts.get(position);
        TextView productId = convertView.findViewById(R.id.productId);
        TextView productName = convertView.findViewById(R.id.productName);
        TextView productPrice = convertView.findViewById(R.id.productPrice);

        productId.setText(String.valueOf(product.getId()));
        productName.setText(product.getName());
        productPrice.setText(String.format("$%.2f", product.getPrice()));

        return convertView;
    }
}
