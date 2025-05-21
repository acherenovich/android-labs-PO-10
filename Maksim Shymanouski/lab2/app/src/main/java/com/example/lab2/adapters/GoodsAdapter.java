package com.example.lab2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.lab2.R;
import com.example.lab2.interfaces.OnChangeListener;
import com.example.lab2.models.Good;

import java.util.List;
public class GoodsAdapter extends BaseAdapter {

    private Context context;
    private List<Good> goodsList;
    private LayoutInflater inflater;
    private OnChangeListener onChangeListener;

    public GoodsAdapter(Context context, List<Good> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        this.onChangeListener = listener;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_good, parent, false);
            holder = new ViewHolder();
            holder.tvGoodId = convertView.findViewById(R.id.tvGoodId);
            holder.tvGoodName = convertView.findViewById(R.id.tvGoodName);
            holder.tvGoodPrice = convertView.findViewById(R.id.tvGoodPrice);
            holder.checkBox = convertView.findViewById(R.id.checkBoxGood);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Good good = goodsList.get(position);

        holder.tvGoodId.setText("ID: " + good.getId());
        holder.tvGoodName.setText("Name: " + good.getName());
        holder.tvGoodPrice.setText("Price: " + good.getPrice());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(good.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                good.setChecked(isChecked);
                if (onChangeListener != null) {
                    onChangeListener.onCheckedChange();
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvGoodId;
        TextView tvGoodName;
        TextView tvGoodPrice;
        CheckBox checkBox;
    }
}
