package com.example.lab1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<University> {
    private Context context;
    private int resource;

    public CustomAdapter(Context context, int resource, ArrayList<University> universities) {
        super(context, resource, universities);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        University university = getItem(position);
        if (university != null) {
            TextView nameTextView = convertView.findViewById(R.id.textViewName);
            TextView countryTextView = convertView.findViewById(R.id.textViewCountry);
            ImageView imageView = convertView.findViewById(R.id.imageView);

            nameTextView.setText(university.getName());
            countryTextView.setText(university.getCountry());
            imageView.setImageResource(R.drawable.img_1); // Убедитесь, что изображение есть в ресурсах
        }

        return convertView;
    }
}