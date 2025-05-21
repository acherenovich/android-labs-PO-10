package com.example.lab1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<University> {
    private int layoutResource;

    public ListViewAdapter(Context context, int resource, ArrayList<University> universities) {
        super(context, resource, universities);
        this.layoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layoutResource, null);
        }

        University university = getItem(position);
        if (university != null) {
            TextView nameTextView = view.findViewById(R.id.textViewName);
            TextView countryTextView = view.findViewById(R.id.textViewCountry);
            nameTextView.setText(university.getName());
            countryTextView.setText(university.getCountry());
        }

        return view;
    }
}