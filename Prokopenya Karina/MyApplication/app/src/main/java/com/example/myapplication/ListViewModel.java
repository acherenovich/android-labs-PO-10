package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class ListViewModel extends ViewModel {

    private final MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();
    private final ApiService apiService;

    // Конструктор ViewModel, создаём Retrofit и ApiService
    public ListViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/cirieena/k/refs/heads/main/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public LiveData<List<Item>> getItems() {
        return itemsLiveData;
    }

    // Метод для загрузки данных
    public void loadItems() {
        apiService.getItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemsLiveData.setValue(response.body()); // Обновляем данные в LiveData
                } else {
                    itemsLiveData.setValue(null); // Если неудачный ответ
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                itemsLiveData.setValue(null); // Если ошибка сети
            }
        });
    }
}
