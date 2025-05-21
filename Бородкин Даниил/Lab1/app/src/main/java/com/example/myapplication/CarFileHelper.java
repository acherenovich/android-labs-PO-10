package com.example.myapplication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CarFileHelper {

    public static List<Car> readCarsFromJsonFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Car> carList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            reader.close();

            Gson gson = new Gson();
            carList = gson.fromJson(stringBuilder.toString(), new TypeToken<List<Car>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return carList;
    }
}
