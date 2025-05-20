//package com.example.lab17;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.List;
//
//public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
//    private List<String> imagePaths;
//    private Context context;
//
//    public GalleryAdapter(Context context, List<String> imagePaths) {
//        this.context = context;
//        this.imagePaths = imagePaths;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String imagePath = imagePaths.get(position);
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        holder.imageView.setImageBitmap(bitmap);
//    }
//
//    @Override
//    public int getItemCount() {
//        return imagePaths.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.imageView);
//        }
//    }
//}