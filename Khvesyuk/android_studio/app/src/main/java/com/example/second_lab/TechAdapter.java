package com.example.second_lab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class TechAdapter extends BaseAdapter {
    private Context context;
    private List<TechItem> techItems;
    private TextView tvSelectedCount; // Получаем TextView

    public TechAdapter(Context context, List<TechItem> techItems, TextView tvSelectedCount) {
        this.context = context;
        this.techItems = techItems;
        this.tvSelectedCount = tvSelectedCount;
    }

    @Override
    public int getCount() {
        return techItems.size();
    }

    @Override
    public Object getItem(int position) {
        return techItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tech, parent, false);
        }

        TechItem item = techItems.get(position);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        tvName.setText(item.getName());
        tvPrice.setText(String.format("$%.2f", item.getPrice()));
        checkBox.setChecked(item.isChecked());

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            updateSelectedCount();
        });

        return convertView;
    }


    private void updateSelectedCount() {
        int count = 0;
        for (TechItem item : techItems) {
            if (item.isChecked()) {
                count++;
            }
        }
        tvSelectedCount.setText("Выбрано: " + count);
    }
}

