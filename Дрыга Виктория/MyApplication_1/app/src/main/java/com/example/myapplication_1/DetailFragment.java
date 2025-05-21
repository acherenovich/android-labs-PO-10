package com.example.myapplication_1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


public class DetailFragment extends Fragment {
    private TextView landmarkNameTextView, landmarkDescriptionTextView, landmarkLocationTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_landmark_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        landmarkNameTextView = view.findViewById(R.id.tv_landmark_name);
        landmarkDescriptionTextView = view.findViewById(R.id.tv_landmark_description);
        landmarkLocationTextView = view.findViewById(R.id.tv_landmark_location);

        Landmark selectedLandmark = SelectedLandmarkHolder.getInstance().getSelectedLandmark();

        if (selectedLandmark != null) {
            setSelectedItem(selectedLandmark);
        }
    }

    public void setSelectedItem(Landmark landmark) {
        landmarkNameTextView.setText(landmark.getName());
        landmarkDescriptionTextView.setText(landmark.getDescription());
        landmarkLocationTextView.setText("Местоположение: " + landmark.getLocation());

        ImageView imageView = getView().findViewById(R.id.iv_landmark_image);
        Glide.with(getContext())
                .load(landmark.getImageUrl()) // предполагается, что это URL изображения
                .into(imageView);
    }  }

