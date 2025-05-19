package com.example.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layout;
    private List<Product> products;

    public CartAdapter(Context context, int resource, List<Product> products) {
        this.products = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    // количество элементов
    @Override
    public int getCount() {
        return products.size();
    }
    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(layout, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product product = products.get(position);

        viewHolder.productNameView.setText(product.getName());
        viewHolder.productIdView.setText(String.valueOf(product.getId()));
        viewHolder.productPriceView.setText(String.format(Locale.getDefault(), "%.2f", product.getPrice()) + " р");

        return convertView;
    }

    private class ViewHolder {
        final TextView productNameView, productIdView, productPriceView;
        ViewHolder(View view) {
            productNameView = view.findViewById(R.id.prName);
            productIdView = view.findViewById(R.id.prId);
            productPriceView = view.findViewById(R.id.prPrice);
        }
    }
}
