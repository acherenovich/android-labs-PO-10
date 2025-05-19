package com.example.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;
    private LayoutInflater inflater;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return products.size(); }

    @Override
    public Object getItem(int position) { return products.get(position); }

    @Override
    public long getItemId(int position) { return products.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_product, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.productImage);
        TextView nameView = convertView.findViewById(R.id.productName);
        TextView priceView = convertView.findViewById(R.id.productPrice);
        CheckBox checkBox = convertView.findViewById(R.id.productCheckBox);

        Product product = products.get(position);

        imageView.setImageResource(product.getImageResId());
        nameView.setText(product.getName());
        priceView.setText(product.getPrice() + " руб.");

        checkBox.setChecked(product.isChecked());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> product.setChecked(isChecked));

        return convertView;
    }
}

