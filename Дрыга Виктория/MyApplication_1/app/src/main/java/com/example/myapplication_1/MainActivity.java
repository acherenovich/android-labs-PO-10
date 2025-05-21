package com.example.myapplication_1;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import android.content.Intent;
import android.content.res.Configuration;
import java.io.FileOutputStream;
import java.io.IOException;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView recyclerView;
    private LandmarkAdapter adapter;
    private List<Landmark> landmarkList = new ArrayList<>();
    private static final String URL = "http://192.168.100.4:3000/items";

    private int currentPage = 1;
    private static final int ITEMS_PER_PAGE = 5;
    private Button btnNext, btnPrev, btnDownload, btnFetch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnNext = findViewById(R.id.btn_next);
        Button btnPrev = findViewById(R.id.btn_prev);
        btnFetch = findViewById(R.id.btn_fetch);
        btnDownload = findViewById(R.id.btn_download);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LandmarkAdapter(landmarkList, this);
        recyclerView.setAdapter(adapter);

        btnNext.setOnClickListener(v -> {
            currentPage++;
            fetchLandmarks();
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                fetchLandmarks();
            }
        });



        btnFetch.setOnClickListener(v -> {
            Log.d("MainActivity", "Кнопка нажата");

            if (landmarkList.isEmpty()) {
                fetchLandmarks();
            } else {
                landmarkList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        // Обработчик кнопки скачивания данных
        btnDownload.setOnClickListener(v -> {
            if (landmarkList.isEmpty()) {
                Toast.makeText(this, "Нет данных для скачивания", Toast.LENGTH_SHORT).show();
                return;
            }

            // Если данные есть, сохраняем их в файл
            saveResponseToFile();
        });



        Button btnFetch = findViewById(R.id.btn_fetch);
        if (btnFetch == null) {
            Log.e("MainActivity", "Кнопка не найдена! Проверь XML.");
        } else {
            Log.d("MainActivity", "Кнопка найдена, устанавливаем слушатель.");
            btnFetch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MainActivity", "Кнопка нажата");

                    if (landmarkList.isEmpty()){
                        btnFetch.setText("Hide Products");
                        fetchLandmarks();
                    } else {
                        landmarkList.clear();
                        adapter.notifyDataSetChanged();
                        btnFetch.setText("Show products");
                    }
                }
            });
        }
    }

    @Override
    public void onItemClick(Landmark landmark) {
        SelectedLandmarkHolder.getInstance().setSelectedLandmark(landmark);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DetailFragment())
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);

        }
    }
    private void fetchLandmarks() {
        String paginatedUrl = URL + "?page=" + currentPage + "&limit=" + ITEMS_PER_PAGE;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, paginatedUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Type listType = new TypeToken<List<Landmark>>() {}.getType();

                        landmarkList.clear();
                        landmarkList.addAll(new Gson().fromJson(response.toString(), listType));
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Ошибка при получении данных", error);
                    }
                });
        queue.add(request);
    }

    private void saveResponseToFile() {
        try {
            File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadFolder.exists()) {
                downloadFolder.mkdir();
            }
            File file = new File(downloadFolder, "landmarks_response.txt");
            FileOutputStream fos = new FileOutputStream(file);

            StringBuilder data = new StringBuilder();
            for (Landmark landmark : landmarkList) {
                data.append(landmark.toString()).append("\n\n");
            }

            fos.write(data.toString().getBytes());
            fos.close();

            Toast.makeText(this, "Данные успешно сохранены в папку Downloads!", Toast.LENGTH_SHORT).show();

            btnDownload.setText("Сохранено");

        } catch (IOException e) {
            Log.e("MainActivity", "Ошибка при записи в файл", e);

            Toast.makeText(this, "Ошибка при сохранении данных", Toast.LENGTH_SHORT).show();
        }
    }

}
