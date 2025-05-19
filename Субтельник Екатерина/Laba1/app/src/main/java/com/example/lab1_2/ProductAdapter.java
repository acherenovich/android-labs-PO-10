package com.example.lab1_2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private OnItemClickListener listener;
    private boolean isGridView;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnItemClickListener listener, boolean isGridView) {
        this.productList = productList;
        this.listener = listener;
        this.isGridView = isGridView;
    }

    public void setGridView(boolean isGridView) {
        this.isGridView = isGridView;
        notifyDataSetChanged();
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

        holder.nameTextView.setText(product.getName());
        holder.categoryTextView.setText("Category: " + product.getCategory());
        holder.priceTextView.setText(String.format("$%.2f", product.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .into(holder.imageView);

        if (isGridView) {
            holder.categoryTextView.setVisibility(View.GONE);
        } else {

            holder.categoryTextView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, categoryTextView, priceTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_product_image);
            nameTextView = itemView.findViewById(R.id.tv_product_name);
            categoryTextView = itemView.findViewById(R.id.tv_categoryTextView);
            priceTextView = itemView.findViewById(R.id.tv_product_price);
        }
    }
}
