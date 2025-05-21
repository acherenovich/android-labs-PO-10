package com.example.myapplication_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.ViewHolder> {
    private List<Landmark> landmarkList;
    private OnItemClickListener onItemClickListener;

    public LandmarkAdapter(List<Landmark> landmarkList, OnItemClickListener listener) {
        this.landmarkList = landmarkList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_landmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Landmark landmark = landmarkList.get(position);

        holder.nameTextView.setText(landmark.getName());
        holder.descriptionTextView.setText(landmark.getDescription());
        holder.locationTextView.setText("Местоположение: " + landmark.getLocation());

        Glide.with(holder.itemView.getContext())
                .load(landmark.getImageUrl())
                .into(holder.imageView);


        holder.itemView.setOnClickListener(v -> {
            v.setAlpha(0.5f);
            v.animate().alpha(1).setDuration(500).start();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(landmark);
            }
        });
    }

    public void updateData(List<Landmark> newLandmarks) {
        this.landmarkList = newLandmarks;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return landmarkList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, locationTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_landmark_name);
            descriptionTextView = itemView.findViewById(R.id.tv_landmark_description);
            locationTextView = itemView.findViewById(R.id.tv_landmark_location);
            imageView = itemView.findViewById(R.id.iv_landmark_image);
        }
    }
}
