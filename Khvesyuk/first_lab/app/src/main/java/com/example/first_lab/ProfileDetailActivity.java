package com.example.first_lab;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.first_lab.model.Profile;

public class ProfileDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        TextView titleView = findViewById(R.id.detail_title);
        TextView descriptionView = findViewById(R.id.detail_description);
        TextView dateView = findViewById(R.id.detail_date);
        TextView countryView = findViewById(R.id.detail_country);
        ImageView imageView = findViewById(R.id.detail_image);

        Profile profile = (Profile) getIntent().getSerializableExtra("profile");

        if (profile != null) {
            titleView.setText(profile.getTitle());
            descriptionView.setText(profile.getDescription());
            dateView.setText("Дата: " + profile.getDate());
            countryView.setText("Страна: " + profile.getCountry());

            Glide.with(this)
                    .load(profile.getImageUrl())
                    .dontAnimate()
                    .into(imageView);
        }
    }
}
