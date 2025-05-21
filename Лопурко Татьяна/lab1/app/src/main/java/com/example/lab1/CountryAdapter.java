package com.example.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    interface OnItemClickListener{
        void onItemClick(Country country);
    }

    private final OnItemClickListener onClickListener;

    private List<Country> countryList;
    private Context context;

    public CountryAdapter(Context context, List<Country> countryList, OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.context = context;
        this.countryList = countryList;
    }

    // Метод для обновления данных
    public void updateData(List<Country> newCountries) {
        this.countryList = newCountries;
        notifyDataSetChanged();  // Уведомляем адаптер, что данные изменились
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.name.setText(country.getName());
        holder.capital.setText("Столица: " + country.getCapital());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                holder.itemView.setAlpha(0.5f); // Устанавливаем начальную прозрачность
                holder.itemView.animate().alpha(1).setDuration(500).start(); // Плавное появление
                onClickListener.onItemClick(country);
            }
        });

        Bitmap cachedImage = ImageCache.get(country.getFlagUrl());
        if (cachedImage != null) {
            holder.flag.setImageBitmap(cachedImage);
        } else {
            holder.flag.setImageResource(R.drawable.picture);
            new Thread(() -> {
                try {
                    Bitmap bitmap = ImageLoader.loadImageFromUrl(country.getFlagUrl());
                    if (bitmap != null) {
                        ImageCache.put(country.getFlagUrl(), bitmap);
                        new Handler(Looper.getMainLooper()).post(() -> holder.flag.setImageBitmap(bitmap));
                    }
                }
                catch (FileNotFoundException e) {
                    showToast("Ошибка загрузки изображения: " + country.getName());
                }
                catch (MalformedURLException e) {
                    Log.e("CountryViewModel", "Неверный URL", e);
                    showToast("Указан некорректный URL");
                } catch (SocketTimeoutException e) {
                    Log.e("CountryViewModel", "Тайм-аут запроса", e);
                    showToast("Сервер не отвечает. Попробуйте позже.");
                } catch (UnknownHostException e) {
                    Log.e("CountryViewModel", "Нет подключения к интернету", e);
                    showToast("Проблема с подключением. Проверьте интернет.");
                } catch (Exception e) {
                    Log.e("CountryViewModel", "Неизвестная ошибка", e);
                    showToast("Произошла ошибка. Попробуйте позже.");
                }
            }).start();
        }
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, capital;
        ImageView flag;

        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.name);
            capital = itemView.findViewById(R.id.capital);
            flag = itemView.findViewById(R.id.flag);
        }
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

}
