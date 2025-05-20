package com.example.myapplication;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("data5.json") // Относительный путь к файлу
    Call<List<Item>> getItems();
}
