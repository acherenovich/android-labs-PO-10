package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> items;
    private OnItemClickListener listener;

    public ItemAdapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    // Интерфейс для обработки кликов
    public interface OnItemClickListener {
        void onItemClick(Item item);  // Метод для обработки кликов
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);

        // Устанавливаем данные в TextView
        holder.title.setText(item.getTitle());
        holder.fear.setText(item.getFear());  // Показываем страх в списке

        // Загружаем изображение
        Picasso.get().load(item.getImageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(item); // Вызываем метод при клике на элемент
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView fear;  // Добавили TextView для страха
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            fear = itemView.findViewById(R.id.item_fear);  // Инициализируем TextView для страха
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}
