package com.example.lab1;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONHelper {

    public static List<Country> parseJson(String jsonString) throws JsonSyntaxException{

        Log.d("JSON", jsonString); // Посмотрите содержимое строки
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Country>>() {}.getType();
        return gson.fromJson(jsonString, listType);

    }
}
