package com.example.lab1_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class ProductDetailFragment extends Fragment {

    private static final String ARG_NAME = "product_name";
    private static final String ARG_DESC = "product_description";
    private static final String ARG_IMAGE = "product_image";
    private static final String ARG_PRICE = "product_price";


    public static ProductDetailFragment newInstance(String name, String description, String imageUrl, double price) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESC, description);
        args.putString(ARG_IMAGE, imageUrl);
        args.putDouble(ARG_PRICE, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail, container, false);

        ImageView imageView = view.findViewById(R.id.iv_detail_image);
        TextView nameText = view.findViewById(R.id.tv_detail_name);
        TextView descText = view.findViewById(R.id.tv_detail_description);
        TextView priceText = view.findViewById(R.id.tv_detail_price);

        if (getArguments() != null) {
            nameText.setText(getArguments().getString(ARG_NAME));
            descText.setText(getArguments().getString(ARG_DESC));
            priceText.setText("Price: " + getArguments().getDouble(ARG_PRICE));

            String imageUrl = getArguments().getString(ARG_IMAGE);
            Glide.with(this).load(imageUrl).into(imageView);
        }

        return view;
    }

}
