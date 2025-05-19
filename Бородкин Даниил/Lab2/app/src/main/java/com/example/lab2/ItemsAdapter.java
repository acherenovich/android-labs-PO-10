package com.example.lab2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{

    private final String CURRENCY = "BYN";
    private CheckedItemsObserver.OnChangeListener onChangeListener;

    private Context context;
    private List<Item> item_List;
    private LayoutInflater inflater;
    private int[] colors;
    private ArrayList<Item> checked_items_array_adapter = new ArrayList<Item>();
    
    public ItemsAdapter(Context context, List<Item> itemList, CheckedItemsObserver.OnChangeListener onChangeListener) {
        Resources res = context.getResources();
        colors = new int[]{
                res.getColor(R.color.primary_blue, null),
                res.getColor(R.color.secondary_orange, null)
        };
        this.context = context;
        this.item_List = itemList;
        this.inflater = LayoutInflater.from(context);
        this.onChangeListener = onChangeListener;
    }

    @Override
    public int getCount() {
        return item_List.size();
    }

    @Override
    public Object getItem(int position) {
        return item_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
        }

        Item item = item_List.get(position);
        int color = getColorForPosition(position);
        convertView.setBackgroundColor(color);
        TextView tv_item_id =  convertView.findViewById(R.id.text_id);
        ImageView imageItem = convertView.findViewById(R.id.imageView);
        TextView tv_item_name = convertView.findViewById(R.id.text_name);
        TextView tv_item_price = convertView.findViewById(R.id.text_price);
        CheckBox ch_item = convertView.findViewById(R.id.item_checkbox);
        tv_item_id.setText(String.valueOf(item.getId()));
        tv_item_name.setText(item.getName());
        tv_item_price.setText(item.getPrice() + CURRENCY);
        ch_item.setChecked(item.getChecked());
        ch_item.setTag(position);
        ch_item.setOnCheckedChangeListener(this);
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

    public ArrayList<Item> getCheckedItems() {
        return checked_items_array_adapter;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton.isShown()) {
            int i = (int) compoundButton.getTag();
            item_List.get(i).setChecked(isChecked);
            notifyDataSetChanged();

            if(isChecked){
                checked_items_array_adapter.add(item_List.get(i));
            }
            else {
                checked_items_array_adapter.remove(item_List.get(i));
            }
            onChangeListener.onDataChanged();
        }
    }

    private int getColorForPosition(int position) {
        return (position % 2 == 0) ? colors[0] : colors[1];
    }

}
