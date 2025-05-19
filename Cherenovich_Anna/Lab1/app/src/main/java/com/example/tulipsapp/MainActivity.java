package com.example.tulipsapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TulipAdapter adapter;
    private List<Tulip> fullTulipList;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 7;
    private Button btnNext, btnPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fullTulipList = new ArrayList<>();
        adapter = new TulipAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        Button btnLoad = findViewById(R.id.btnLoad);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        btnLoad.setOnClickListener(view -> loadTulips());
        btnNext.setOnClickListener(view -> loadNextPage());
        btnPrevious.setOnClickListener(view -> loadPreviousPage());

        // Если экран перевернули, восстанавливаем состояние
        if (savedInstanceState != null) {
            fullTulipList = savedInstanceState.getParcelableArrayList("tulipList");
            currentPage = savedInstanceState.getInt("currentPage", 0);
            updateList();
        } else {
            btnNext.setEnabled(false);
            btnPrevious.setEnabled(false);
        }
    }

    private void loadTulips() {
        TulipApi api = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TulipApi.class);

        Call<List<Tulip>> call = api.getTulips();

        call.enqueue(new Callback<List<Tulip>>() {
            @Override
            public void onResponse(Call<List<Tulip>> call, Response<List<Tulip>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullTulipList.clear();
                    fullTulipList.addAll(response.body());
                    currentPage = 0;
                    updateList();
                }
            }

            @Override
            public void onFailure(Call<List<Tulip>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateList() {
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, fullTulipList.size());

        List<Tulip> paginatedList = new ArrayList<>(fullTulipList.subList(start, end));
        adapter.updateData(paginatedList);

        btnNext.setEnabled((currentPage + 1) * PAGE_SIZE < fullTulipList.size());
        btnPrevious.setEnabled(currentPage > 0);
    }

    private void loadNextPage() {
        if ((currentPage + 1) * PAGE_SIZE < fullTulipList.size()) {
            currentPage++;
            updateList();
        }
    }

    private void loadPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateList();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("tulipList", (ArrayList<? extends Parcelable>) fullTulipList);
        outState.putInt("currentPage", currentPage);
    }
}
