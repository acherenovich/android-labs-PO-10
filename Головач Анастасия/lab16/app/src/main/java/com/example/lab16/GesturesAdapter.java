package com.example.lab16;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class GesturesAdapter extends ArrayAdapter<GesturesAdapter.GestureItem> {

    static class GestureItem {
        String name;
        Gesture gesture;

        GestureItem(String name, Gesture gesture) {
            this.name = name;
            this.gesture = gesture;
        }
    }

    public GesturesAdapter(Context context, List<GestureItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GestureItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gesture_list_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.gesture_name);
        GestureOverlayView gesturePreview = convertView.findViewById(R.id.gesture_preview);

        textViewName.setText(item.name);
        gesturePreview.setGesture(item.gesture);

        return convertView;
    }
}