package com.example.first_lab.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.first_lab.R;
import com.example.first_lab.model.Profile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<Profile> profiles;
    private OnProfileClickListener listener;
    private Context context;

    public interface OnProfileClickListener {
        void onSingleClick(Profile profile);
        void onDoubleClick(Profile profile);
    }

    public ProfileAdapter(List<Profile> profiles, OnProfileClickListener listener, Context context) {
        this.profiles = profiles;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.title.setText(profile.getTitle());
        holder.description.setText(profile.getDescription());

        Glide.with(context)
                .load(profile.getImageUrl())
                .dontAnimate()
                .into(holder.imageView);

        // Добавляем текст в плашку
        holder.detailsText.setText(
                "Название: " + profile.getTitle() + "\n" +
                        "Описание: " + profile.getDescription() + "\n" +
                        "Дата: " + profile.getDate() + "\n" +
                        "Страна: " + profile.getCountry()
        );

        // Обработчик одиночного и двойного клика
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Интервал для двойного клика
            private long lastClickTime = 0;
            private Handler handler = new Handler();
            private Runnable singleClickRunnable;

            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();

                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    if (singleClickRunnable != null) {
                        handler.removeCallbacks(singleClickRunnable);
                    }
                    listener.onDoubleClick(profile);
                    holder.detailsPopup.setVisibility(View.GONE);
                } else {
                    singleClickRunnable = () -> {
                        // Показываем или скрываем плашку
                        if (holder.detailsPopup.getVisibility() == View.VISIBLE) {
                            holder.detailsPopup.setVisibility(View.GONE);
                        } else {
                            holder.detailsPopup.setVisibility(View.VISIBLE);
                        }
                    };
                    handler.postDelayed(singleClickRunnable, DOUBLE_CLICK_TIME_DELTA);
                }
                lastClickTime = clickTime;
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void updateProfiles(List<Profile> newProfiles) {
        profiles.clear();
        profiles.addAll(newProfiles);
        notifyDataSetChanged();
    }

    public List<Profile> getProfiles() {
        return profiles; // Вернёт текущий список профилей
    }
    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, detailsText;
        ImageView imageView;
        LinearLayout detailsPopup;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.profile_title);
            description = itemView.findViewById(R.id.profile_description);
            imageView = itemView.findViewById(R.id.profile_image);
            detailsPopup = itemView.findViewById(R.id.details_popup);
            detailsText = itemView.findViewById(R.id.details_text);
        }
    }
}
