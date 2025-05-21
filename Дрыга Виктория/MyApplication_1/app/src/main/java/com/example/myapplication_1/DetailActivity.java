package com.example.myapplication_1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private TextView landmarkNameTextView;
    private TextView landmarkDescriptionTextView;
    private TextView landmarkLocationTextView;
    private ImageView landmarkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        landmarkNameTextView = findViewById(R.id.tv_landmark_name);
        landmarkDescriptionTextView = findViewById(R.id.tv_landmark_description);
        landmarkLocationTextView = findViewById(R.id.tv_landmark_location);
        landmarkImageView = findViewById(R.id.iv_landmark_image);

        Landmark landmark = SelectedLandmarkHolder.getInstance().getSelectedLandmark();

        if (landmark != null) {
            landmarkNameTextView.setText(landmark.getName());
            landmarkDescriptionTextView.setText(landmark.getDescription());
            landmarkLocationTextView.setText("Местоположение: " + landmark.getLocation());

            Glide.with(this)
                    .load(landmark.getImageUrl())
                    .into(landmarkImageView);
        }
    }
}
