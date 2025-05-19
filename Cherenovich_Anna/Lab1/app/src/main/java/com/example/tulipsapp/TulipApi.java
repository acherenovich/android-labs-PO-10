package com.example.tulipsapp;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

// Интерфейс для взаимодействия с API
public interface TulipApi {
    // Метод для получения данных
    @GET("acherenovich/tulips/6b534ace26467cc120b9d4d0923e8ee8eabd5e31/data.json")
    Call<List<Tulip>> getTulips();
}
