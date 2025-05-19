package com.example.second_lab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<TechItem> cartItems;

    public CartAdapter(Context context, List<TechItem> cartItems) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        }

        TechItem item = cartItems.get(position);

        TextView tvCartName = convertView.findViewById(R.id.tvCartName);
        TextView tvCartPrice = convertView.findViewById(R.id.tvCartPrice);

        tvCartName.setText(item.getName());
        tvCartPrice.setText(String.format("$%.2f", item.getPrice()));

        return convertView;
    }
}
