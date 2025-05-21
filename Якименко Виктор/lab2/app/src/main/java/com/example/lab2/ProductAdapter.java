package com.example.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;
    private LayoutInflater inflater;
    private TextView checkedCountTextView;

    public ProductAdapter(Context context, List<Product> productList, TextView checkedCountTextView) {
        this.context = context;
        this.productList = productList;
        this.inflater = LayoutInflater.from(context);
        this.checkedCountTextView = checkedCountTextView;
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
            convertView = inflater.inflate(R.layout.list_item_product, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.textViewProductName);
            holder.priceTextView = convertView.findViewById(R.id.textViewProductPrice);
            holder.checkBox = convertView.findViewById(R.id.checkBoxProduct);
            holder.buttonDecrement = convertView.findViewById(R.id.buttonDecrement);
            holder.buttonIncrement = convertView.findViewById(R.id.buttonIncrement);
            holder.quantityTextView = convertView.findViewById(R.id.textViewQuantity);
            holder.imageView = convertView.findViewById(R.id.imageViewProduct);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);

        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText("Цена: " + product.getPrice());
        holder.checkBox.setChecked(product.isChecked());
        holder.quantityTextView.setText(String.valueOf(product.getQuantity()));

        // Загрузка изображения с помощью Glide
        Glide.with(context)
                .load(product.getImagePath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .centerCrop())
                .into(holder.imageView);


        holder.buttonDecrement.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            if (quantity > 1) {
                product.setQuantity(quantity - 1);
                holder.quantityTextView.setText(String.valueOf(product.getQuantity()));
                if (product.isChecked()) {
                    updateCheckedCount();
                }
            }
        });

        holder.buttonIncrement.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            holder.quantityTextView.setText(String.valueOf(product.getQuantity()));
            if (product.isChecked()) {
                updateCheckedCount();
            }
        });


        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setChecked(isChecked);
            updateCheckedCount();
        });

        return convertView;
    }

    private void updateCheckedCount() {
        int count = 0;
        for (Product p : productList) {
            if (p.isChecked()) {
                count += p.getQuantity(); // Суммируем количества выбранных товаров
            }
        }
        checkedCountTextView.setText("Выбрано: " + count);
    }


    static class ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        CheckBox checkBox;
        ImageButton buttonDecrement;
        ImageButton buttonIncrement;
        TextView quantityTextView;
        ImageView imageView;
    }

    public List<Product> getCheckedProducts() {
        List<Product> checkedProducts = new ArrayList<>();
        for (Product p : productList) {
            if (p.isChecked()) {
                checkedProducts.add(p);
            }
        }
        return checkedProducts;
    }
}