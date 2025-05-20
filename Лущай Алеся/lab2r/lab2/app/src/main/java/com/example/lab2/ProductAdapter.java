package com.example.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{
    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private List<Product> products;
    private OnSelectionChangedListener selectionChangedListener;


    // Интерфейс для уведомления MainActivity о количестве выбранных товаров
    public interface OnSelectionChangedListener {
        public void onSelectionChanged(int selectedCount);
    }

    public ProductAdapter(Context context, int resource, List<Product> products) {
        this.products = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.selectionChangedListener = (OnSelectionChangedListener) context;
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

        viewHolder.checkBox.setChecked(product.isChecked());
        viewHolder.checkBox.setTag(position); // Используем позицию для идентификации
        viewHolder.checkBox.setOnCheckedChangeListener(this); // Устанавливаем новый слушатель

        // Изменяем цвет фона через один
        int colorResId = (position % 2 == 0) ? R.color.light_gray : R.color.white;
        convertView.setBackgroundColor(ContextCompat.getColor(context, colorResId));

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton.isShown()) {
            int position = (int) compoundButton.getTag(); // Получаем индекс элемента
            products.get(position).setChecked(isChecked); // Обновляем состояние товара

            // Уведомляем MainActivity о изменении количества выбранных товаров
            if (selectionChangedListener != null) {
                selectionChangedListener.onSelectionChanged(getSelectedCount());
            }
            //notifyDataSetChanged();  // Перерисовываем список
        }
    }

    // Возвращаем количество выбранных товаров
    private int getSelectedCount() {
        int count = 0;
        for (Product product : products) {
            if (product.isChecked()) {
                count++;
            }
        }
        return count;
    }

    private class ViewHolder {
        final TextView productNameView, productIdView, productPriceView;
        final CheckBox checkBox;
        ViewHolder(View view) {
            productNameView = view.findViewById(R.id.prName);
            productIdView = view.findViewById(R.id.prId);
            productPriceView = view.findViewById(R.id.prPrice);
            checkBox = view.findViewById(R.id.cbSelect);
        }
    }

}