package com.example.first_lab.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {
    @SerializedName("data")
    private List<Profile> data;

    @SerializedName("successful")
    private boolean successful;

    @SerializedName("error")
    private String error;

    public List<Profile> getData() {
        return data;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getError() {
        return error;
    }
}
