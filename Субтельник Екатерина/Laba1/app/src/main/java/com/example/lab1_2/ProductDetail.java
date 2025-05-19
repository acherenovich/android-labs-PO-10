package com.example.lab1_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        TextView nameTextView = findViewById(R.id.tv_detail_name);
        TextView descriptionTextView = findViewById(R.id.tv_detail_description);
        TextView priceTextView = findViewById(R.id.tv_detail_price);
        TextView categoryTextView = findViewById(R.id.tv_detail_category);
        TextView ratingTextView = findViewById(R.id.tv_detail_rating);
        ImageView imageView = findViewById(R.id.iv_detail_image);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("product_name");
            String description = intent.getStringExtra("product_description");
            String imageUrl = intent.getStringExtra("product_image");
            double price = intent.getDoubleExtra("product_price", 0.0);
            String category = intent.getStringExtra("product_category");
            double rating = intent.getDoubleExtra("product_rating", 0.0);

            if (name != null) nameTextView.setText(name);
            if (description != null) descriptionTextView.setText(description);
            priceTextView.setText(String.format("$%.2f", price));

            if (category != null) categoryTextView.setText("Category: " + category);
            ratingTextView.setText("Rating: " + rating + "/5");

            Glide.with(this).load(imageUrl).into(imageView);
        }
    }
}
