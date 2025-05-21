package com.example.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ItemDetailFragment extends Fragment {

    private static final String ARG_ITEM = "item";

    private Item item;

    public static ItemDetailFragment newInstance(Item item) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if (getArguments() != null) {
            item = (Item) getArguments().getSerializable(ARG_ITEM);
        }

        if (item != null) {
            TextView titleTextView = view.findViewById(R.id.item_title);
            TextView descriptionTextView = view.findViewById(R.id.item_description);

            titleTextView.setText(item.getTitle());
            descriptionTextView.setText(item.getDescription());
        }

        return view;
    }
}
