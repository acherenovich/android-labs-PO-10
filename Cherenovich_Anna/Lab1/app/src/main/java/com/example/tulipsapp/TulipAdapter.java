package com.example.tulipsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TulipAdapter extends RecyclerView.Adapter<TulipAdapter.TulipViewHolder> {
    private Context context;
    private List<Tulip> tulipList;



    public TulipAdapter(Context context, List<Tulip> tulipList) {
        this.context = context;
        this.tulipList = tulipList;
    }

    @NonNull
    @Override
    public TulipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tulip, parent, false);
        return new TulipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TulipViewHolder holder, int position) {
        Tulip tulip = tulipList.get(position);
        holder.tulipName.setText(tulip.getName());
        holder.tulipColor.setText(tulip.getColor());
        Picasso.get().load(tulip.getImageUrl()).into(holder.tulipImage);

        // Обработчик клика для перехода в DetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("tulip", tulip); // Передаем объект Tulip
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tulipList.size();
    }

    // Метод для обновления данных в адаптере
    public void updateData(List<Tulip> newTulipList) {
        this.tulipList = newTulipList;
        notifyDataSetChanged();
    }

    public static class TulipViewHolder extends RecyclerView.ViewHolder {
        TextView tulipName, tulipColor;
        ImageView tulipImage;

        public TulipViewHolder(@NonNull View itemView) {
            super(itemView);
            tulipName = itemView.findViewById(R.id.tulipName);
            tulipColor = itemView.findViewById(R.id.tulipColor);
            tulipImage = itemView.findViewById(R.id.tulipImage);
        }
    }

}
