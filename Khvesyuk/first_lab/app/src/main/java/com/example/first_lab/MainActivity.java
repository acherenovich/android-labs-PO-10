package com.example.first_lab;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.widget.Toast;



import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.first_lab.viewmodel.ProfileViewModel;
import com.example.first_lab.adapter.ProfileAdapter;
import com.example.first_lab.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProfileViewModel viewModel;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private Button fetchDataButton;
    private List<Profile> allProfiles = new ArrayList<>();

    private void showProfileDetails(Profile profile) {
        TextView detailView = findViewById(R.id.profile_details);
        detailView.setVisibility(View.VISIBLE);
        detailView.setText("Название: " + profile.getTitle() + "\n" +
                "Описание: " + profile.getDescription() + "\n" +
                "Дата: " + profile.getDate() + "\n" +
                "Страна: " + profile.getCountry());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchDataButton = findViewById(R.id.fetch_data_button);

        adapter = new ProfileAdapter(new ArrayList<>(), new ProfileAdapter.OnProfileClickListener() {
            @Override
            public void onSingleClick(Profile profile) {
                showProfileDetails(profile);
            }

            @Override
            public void onDoubleClick(Profile profile) {
                Intent intent = new Intent(MainActivity.this, ProfileDetailActivity.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
            }
        }, this);

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        viewModel.getProfiles().observe(this, profiles -> {
            if (profiles == null || profiles.isEmpty()) {
                Log.e("MainActivity", "Ошибка: список профилей пуст!");
                return;
            }
            Log.d("MainActivity", "Получено профилей: " + profiles.size());
            allProfiles.clear();
            allProfiles.addAll(profiles);
        });

        fetchDataButton.setOnClickListener(v -> showNumberInputDialog());
    }

    private void showNumberInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите количество элементов");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("ОК", (dialog, which) -> {
            String value = input.getText().toString().trim();
            if (value.isEmpty()) {
                showErrorDialog("Введите число!");
                return;
            }

            int count = Integer.parseInt(value);
            if (count <= 0) {
                showErrorDialog("Число должно быть больше 0!");
            } else {
                fetchProfiles(count);
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage(message)
                .setPositiveButton("ОК", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void fetchProfiles(int count) {
        Log.d("MainActivity", "Запрос данных с сервера...");
        viewModel.fetchProfiles();

        viewModel.getProfiles().observe(this, profiles -> {
            if (profiles == null || profiles.isEmpty()) {
                showErrorDialog("Данные не загружены!");
                return;
            }
            int limit = Math.min(count, profiles.size());
            adapter.updateProfiles(profiles.subList(0, limit));
        });
    }
}