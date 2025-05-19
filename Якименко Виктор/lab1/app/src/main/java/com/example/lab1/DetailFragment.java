package com.example.lab1;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class DetailFragment extends Fragment {

    // Ключи для передачи данных через Bundle
    private static final String ARG_ID = "arg_id";
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_DESCRIPTION = "arg_description";
    private static final String ARG_IMAGE = "arg_image";

    private String title;
    private String description;
    private String imageUrl; // Добавляем переменную для URL изображения

    public DetailFragment() {
        // Обязательный пустой конструктор
    }

    // Метод для создания нового экземпляра с передачей данных
    public static DetailFragment newInstance(Item item) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, item.getId());
        args.putString(ARG_TITLE, item.getTitle());
        args.putString(ARG_DESCRIPTION, item.getDescription());
        args.putString(ARG_IMAGE, item.getImage());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            description = getArguments().getString(ARG_DESCRIPTION);
            imageUrl = getArguments().getString(ARG_IMAGE); // Получаем URL изображения
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView tvTitle = view.findViewById(R.id.tvDetailTitle);
        TextView tvDescription = view.findViewById(R.id.tvDetailDescription);
        ImageView ivDetailImage = view.findViewById(R.id.ivDetailImage); // Находим ImageView

        tvTitle.setText(title);
        tvDescription.setText(description);

        // Загружаем изображение с помощью Glide
        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // Обработка ошибки загрузки (можно, например, установить картинку-заглушку)
                        ivDetailImage.setImageResource(R.drawable.ic_launcher_background); // Устанавливаем заглушку
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Изображение успешно загружено
                        return false;
                    }
                })
                .into(ivDetailImage);

        return view;
    }
}