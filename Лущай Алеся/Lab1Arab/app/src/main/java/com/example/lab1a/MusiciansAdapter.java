package com.example.lab1a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MusiciansAdapter extends ArrayAdapter<Musicians> {
    private LayoutInflater inflater;
    private int layout;
    private List<Musicians> musicians;

    public MusiciansAdapter(Context context, int resource, List<Musicians> musicians) {
        super(context, resource, musicians);
        this.musicians = musicians;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Musicians musician = musicians.get(position);
        viewHolder.imageView.setImageResource(musician.getLogoResource());
        viewHolder.nameView.setText(musician.getName());
        viewHolder.frontmanView.setText(musician.getFrontman());
        return convertView;
    }


    private class ViewHolder {
        final ImageView imageView;
        final TextView nameView, frontmanView;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.logo);
            nameView = view.findViewById(R.id.name);
            frontmanView = view.findViewById(R.id.frontman);
        }
    }
}

