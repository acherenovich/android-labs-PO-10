package com.example.myapplication;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_URL = "image_url";

    public static DetailFragment newInstance(String title, String description, String imageUrl) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);  // исправлено
        args.putString(ARG_DESCRIPTION, description);  // исправлено
        args.putString(ARG_IMAGE_URL, imageUrl);  // исправлено
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Включаем меню
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView title = view.findViewById(R.id.detail_title);
        TextView description = view.findViewById(R.id.detail_description);
        ImageView imageView = view.findViewById(R.id.detail_image);

        String itemTitle = getArguments() != null ? getArguments().getString(ARG_TITLE) : "";
        String itemDescription = getArguments() != null ? getArguments().getString(ARG_DESCRIPTION) : "";
        String imageUrl = getArguments() != null ? getArguments().getString(ARG_IMAGE_URL) : "";

        title.setText(itemTitle);
        description.setText(itemDescription);

        // Загружаем картинку с использованием Picasso
        Picasso.get().load(imageUrl).into(imageView);

        // Включаем кнопку назад в тулбаре
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


