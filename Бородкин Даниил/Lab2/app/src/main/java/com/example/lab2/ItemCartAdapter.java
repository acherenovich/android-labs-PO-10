package com.example.lab2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
public class ItemCartAdapter extends BaseAdapter {

    private final String CURRENCY = "BYN";
    private Context context;
    private ArrayList<Item> itemList;


    public ItemCartAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout_cart, parent, false);
        }


        Item item = itemList.get(position);


        TextView idItem = convertView.findViewById(R.id.text_id2);
        ImageView imageItem = convertView.findViewById(R.id.imageView2);
        TextView nameItem = convertView.findViewById(R.id.text_name2);
        TextView priceItem = convertView.findViewById(R.id.text_price2);


        idItem.setText(String.valueOf(item.getId()));
        nameItem.setText(item.getName());
        priceItem.setText(item.getPrice() + CURRENCY);


        String filename = item.getName() + ".jpg";
        try (InputStream inputStream = context.getAssets().open(filename)) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageItem.setImageDrawable(drawable);
            imageItem.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (IOException e) {
            e.printStackTrace();

        }

        return convertView;
    }
}
