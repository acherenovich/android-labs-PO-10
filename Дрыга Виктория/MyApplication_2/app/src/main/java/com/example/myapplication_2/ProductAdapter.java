package com.example.myapplication_2;

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
    private OnProductCheckedChangeListener listener;

    public interface OnProductCheckedChangeListener {
        void onCheckedChanged();
    }

    public ProductAdapter(Context context, List<Product> productList, OnProductCheckedChangeListener listener) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Product product = productList.get(position);
        TextView productId = convertView.findViewById(R.id.product_id);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productPrice = convertView.findViewById(R.id.product_price);
        CheckBox productCheckBox = convertView.findViewById(R.id.selectedCount);

        productId.setText(String.valueOf(product.getId()));
        productName.setText(product.getName());
        productPrice.setText(String.format("$%.2f", product.getPrice()));
        productCheckBox.setChecked(product.isChecked());

        int backgroundColor = (position % 2 == 0) ? 0xFFFFFFFF : 0xFFF7EEFB;
        convertView.setBackgroundColor(backgroundColor);

        productCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setChecked(isChecked);
            listener.onCheckedChanged();
        });

        return convertView;
    }

}
