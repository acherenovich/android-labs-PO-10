package com.example.lab2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.lab2.R;
import com.example.lab2.models.Good;
import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<Good> goodsList;
    private LayoutInflater inflater;

    public CartAdapter(Context context, List<Good> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
        inflater = LayoutInflater.from(context);
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
        return goodsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_cart_good, parent, false);
            holder = new ViewHolder();
            holder.tvGoodId = convertView.findViewById(R.id.tvGoodId);
            holder.tvGoodName = convertView.findViewById(R.id.tvGoodName);
            holder.tvGoodPrice = convertView.findViewById(R.id.tvGoodPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Good good = goodsList.get(position);
        holder.tvGoodId.setText("ID: " + good.getId());
        holder.tvGoodName.setText("Name: " + good.getName());
        holder.tvGoodPrice.setText("Price: " + good.getPrice());

        return convertView;
    }

    static class ViewHolder {
        TextView tvGoodId;
        TextView tvGoodName;
        TextView tvGoodPrice;
    }
}
