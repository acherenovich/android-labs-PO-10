package com.example.first_lab.network;

import com.example.first_lab.model.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/spy/list")
    Call<ApiResponse> getProfiles();
}
