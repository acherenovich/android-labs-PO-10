package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {
    private TextView txtName, txtDescription, txtPrice;
    private Button btnShare;

    public static DetailFragment newInstance(String name, String description, double price) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("description", description);
        args.putDouble("price", price);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        txtName = view.findViewById(R.id.txtName);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtPrice = view.findViewById(R.id.txtPrice);
        btnShare = view.findViewById(R.id.btnShare);

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String description = getArguments().getString("description");
            double price = getArguments().getDouble("price");

            txtName.setText(name);
            txtDescription.setText(description);
            txtPrice.setText(String.format("$%.2f", price));

            btnShare.setOnClickListener(v -> shareProduct(name, description, price));
        }

        return view;
    }

    private void shareProduct(String name, String description, double price) {
        String message = "Продукт: " + name + "\nОписание: " + description + "\nЦена: $" + price;

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        startActivity(Intent.createChooser(sendIntent, "Поделиться через"));
    }
}
