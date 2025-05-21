package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private LayoutInflater inflater;
    List<Car> carList = new ArrayList<Car>();

    public CarAdapter(LayoutInflater inflater, List<Car> carList) {
        this.inflater = inflater;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.car_layout,parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CarAdapter.ViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.car_information.setText(car.getName() + " (" + car.getYear() + ") - " + car.getPrice() + " $");
        holder.image_view.setImageBitmap(ImageConverter.getImageFromBase64(car.getImage()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView car_information;
        final ImageView image_view;

        public ViewHolder(View itemView) {
            super(itemView);
            car_information = itemView.findViewById(R.id.car_info);
            image_view = itemView.findViewById(R.id.car_image);
        }
    }
}
