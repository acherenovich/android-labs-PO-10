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
    private List<Product> productList;
    private LayoutInflater inflater;
    private OnProductCheckedChangeListener listener;

    public interface OnProductCheckedChangeListener {
        void onCheckedChanged();
    }

    public ProductAdapter(Context context, List<Product> products, OnProductCheckedChangeListener listener) {
        this.context = context;
        this.productList = products;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return productList.size(); }

    @Override
    public Object getItem(int position) { return productList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_product, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.productName);
            holder.price = convertView.findViewById(R.id.productPrice);
            holder.checkBox = convertView.findViewById(R.id.productCheckBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(String.format("$%.2f", product.getPrice()));
        holder.checkBox.setChecked(product.isChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setChecked(isChecked);
            listener.onCheckedChanged();
        });

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView price;
        CheckBox checkBox;
    }
}
