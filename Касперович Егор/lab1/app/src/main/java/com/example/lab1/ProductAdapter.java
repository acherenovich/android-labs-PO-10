package com.example.lab1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private FragmentActivity activity;
    private int selectedPosition = RecyclerView.NO_POSITION; // Индекс выделенного элемента

    public ProductAdapter(List<Product> productList, FragmentActivity activity) {
        this.productList = productList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtDescription.setText(product.getDescription());
        holder.txtPrice.setText("$" + product.getPrice());

        // Устанавливаем цвет фона для выделенного элемента
        holder.itemView.setBackgroundColor(selectedPosition == holder.getAdapterPosition() ? Color.LTGRAY : Color.TRANSPARENT);

        // Загрузка изображения с помощью Glide
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())

                .into(holder.imgProduct);

        // Обработчик клика
        holder.itemView.setOnClickListener(v -> {
            int prevPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition(); // Получаем актуальную позицию

            if (prevPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(prevPosition); // Обновляем старый выделенный элемент
            }
            notifyItemChanged(selectedPosition); // Обновляем новый выделенный элемент

            // Открываем фрагмент с деталями
            DetailFragment detailFragment = DetailFragment.newInstance(
                    product.getName(), product.getDescription(), product.getPrice()
            );

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDescription, txtPrice;
        ImageView imgProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
