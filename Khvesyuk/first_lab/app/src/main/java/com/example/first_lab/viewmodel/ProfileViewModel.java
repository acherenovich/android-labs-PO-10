package com.example.first_lab.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.first_lab.model.Profile;
import com.example.first_lab.network.ApiService;
import com.example.first_lab.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.first_lab.model.ApiResponse;

import android.util.Log;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<List<Profile>> profileList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<List<Profile>> getProfiles() {
        return profileList;
    }

    public LiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }


    public void fetchProfiles() {
        isLoading.setValue(true);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Log.d("ProfileViewModel", "Отправка запроса на сервер...");

        apiService.getProfiles().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ProfileViewModel", "Ответ сервера: " + response.body().toString());

                    if (response.body().isSuccessful()) {
                        profileList.setValue(response.body().getData());
                    } else {
                        Log.e("ProfileViewModel", "Ошибка API: " + response.body().getError());
                    }
                } else {
                    Log.e("ProfileViewModel", "Ошибка сервера: " + response.code());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ProfileViewModel", "Ошибка сети: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }
}
