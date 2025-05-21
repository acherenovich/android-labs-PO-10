package com.example.lab1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Button btnLoadData;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private ImageButton btnSettings;
    private ImageButton btnSave;
    private String jsonString;
    private Switch switchMode;

    // Пагинация
    private Button btnPrevPage;
    private Button btnNextPage;
    private TextView tvPageInfo;
    private int currentPage = 1;
    private int totalPages = 1;
    private int itemsPerPage = 10; // Элементов на странице
    private List<Item> allItems = new ArrayList<>(); // Все загруженные данные

    // Ключи для сохранения состояния
    private static final String KEY_ALL_ITEMS = "all_items";
    private static final String KEY_JSON_STRING = "json_string";
    private static final String KEY_CURRENT_PAGE = "current_page";
    private static final String KEY_TOTAL_PAGES = "total_pages";

    private static final String DATA_URL = "https://api.jsonbin.io/v3/b/67e3e73b8a456b79667d0933?meta=false";

    public ListFragment() {
        // Обязательный пустой конструктор
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Сохраняем данные, только если они есть
        if (allItems != null && !allItems.isEmpty()) {
            // Сериализуем allItems в JSON
            Gson gson = new Gson();
            String jsonItems = gson.toJson(allItems);
            outState.putString(KEY_ALL_ITEMS, jsonItems);

            // Сохраняем jsonString (если нужно)
            outState.putString(KEY_JSON_STRING, jsonString);

            // Сохраняем состояние пагинации
            outState.putInt(KEY_CURRENT_PAGE, currentPage);
            outState.putInt(KEY_TOTAL_PAGES, totalPages);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnLoadData = view.findViewById(R.id.btnLoadData);
        progressBar = view.findViewById(R.id.progressBar);
        errorTextView = view.findViewById(R.id.errorTextView);
        btnSave = view.findViewById(R.id.btnSave);
        switchMode = view.findViewById(R.id.switchMode); // Инициализация Switch


        btnSettings = view.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Пагинация - инициализация
        btnPrevPage = view.findViewById(R.id.btnPrevPage);
        btnNextPage = view.findViewById(R.id.btnNextPage);
        tvPageInfo = view.findViewById(R.id.tvPageInfo);

        btnPrevPage.setOnClickListener(v -> changePage(-1));
        btnNextPage.setOnClickListener(v -> changePage(1));


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(null, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                DetailFragment detailFragment = DetailFragment.newInstance(item);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        recyclerView.setAdapter(adapter);

        // Восстанавливаем состояние, если оно есть  (ПЕРЕНЕСЛИ СЮДА)
        if (savedInstanceState != null) {
            String jsonItems = savedInstanceState.getString(KEY_ALL_ITEMS);
            if (jsonItems != null) {
                // Десериализуем JSON обратно в List<Item>
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Item>>() {}.getType();
                allItems = gson.fromJson(jsonItems, listType);

                // Восстанавливаем jsonString (если нужно)
                jsonString = savedInstanceState.getString(KEY_JSON_STRING);

                // Восстанавливаем состояние пагинации
                currentPage = savedInstanceState.getInt(KEY_CURRENT_PAGE);
                totalPages = savedInstanceState.getInt(KEY_TOTAL_PAGES);

                // !!! ДОБАВЛЯЕМ ПРОВЕРКУ !!!
                if (allItems.isEmpty()) {
                    currentPage = 1;
                    totalPages = 1;
                } else {
                    totalPages = (int) Math.ceil((double) allItems.size() / itemsPerPage);
                    if (currentPage > totalPages) {
                        currentPage = totalPages; // Корректируем currentPage
                    }
                }

                // Отображаем данные и обновляем UI
                displayCurrentPage();  // !!! ВАЖНО !!!
            }
        }
        updatePageInfo(); // Обновляем UI *после* восстановления (или инициализации)
        displayCurrentPage();

        btnLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchMode.isChecked()) {
                    loadDataFromFile();
                } else {
                    loadDataFromServer();
                }
            }
        });
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loadDataFromFile();
                } else {
                    loadDataFromServer();
                }
            }
        });

        btnSave.setOnClickListener(v -> saveJsonToFile());
        setEnableBtnSave(false);

        return view;
    }

    private void loadDataFromServer() {
        resetPagination(); // Сброс пагинации
        errorTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        adapter.setItems(new ArrayList<>()); //Очистка адаптера
        adapter.notifyDataSetChanged();  // Важно! Уведомить адаптер
        setEnableBtnSave(false);

        SharedPreferences preferences = getActivity().getSharedPreferences("AppSettings", getContext().MODE_PRIVATE);
        String serverUrl = preferences.getString("server_url", DATA_URL);
        int rowsCount = preferences.getInt("rows_count", 10);
        itemsPerPage = rowsCount;  // Обновляем itemsPerPage

        new FetchDataTask().execute(serverUrl, Integer.toString(rowsCount));
    }


    private void loadDataFromFile() {
        resetPagination(); //Сброс пагинации
        errorTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        adapter.setItems(new ArrayList<>()); // Очистка адаптера
        adapter.notifyDataSetChanged(); //Важно уведомить

        new LoadDataFromFileTask().execute();
    }
    private void resetPagination() {
        currentPage = 1;
        totalPages = 1;
        allItems.clear(); // Очищаем список всех элементов
        updatePageInfo(); // Обновляем UI
    }

    private void saveJsonToFile() {
        if (jsonString == null || jsonString.isEmpty()) return;

        File file = new File(getActivity().getExternalFilesDir(null), "data.txt");
        Log.i("save txt","txt file path: " + file.getPath());
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonString);
            Toast.makeText(getContext(), "JSON сохранен в data.txt!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }

    private void setEnableBtnSave(boolean enable) {
        if (enable) {
            btnSave.setEnabled(true);
            btnSave.clearColorFilter();
        } else {
            btnSave.setEnabled(false); // Изначально неактивна
            btnSave.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }
    }


    private void changePage(int direction) {
        currentPage += direction;
        if (currentPage < 1) {
            currentPage = 1;
        }
        // Добавляем проверку на размер allItems
        else if (currentPage > totalPages || allItems.isEmpty()) { //Добавили allItems.isEmpty()
            currentPage = totalPages;
            if(allItems.isEmpty()){
                currentPage = 1; //Если данных нет, то 1
            }
        }
        updatePageInfo();
        displayCurrentPage();
    }


    private void updatePageInfo() {
        tvPageInfo.setText("Стр. " + currentPage + "/" + totalPages);
        btnPrevPage.setEnabled(currentPage > 1);
        btnNextPage.setEnabled(currentPage < totalPages);
    }


    private void displayCurrentPage() {
        if (allItems.isEmpty()) {
            adapter.setItems(new ArrayList<>()); //Очищаем адаптер
            adapter.notifyDataSetChanged(); //Уведомляем
            return; // Ничего не делаем, если данных нет.
        }

        int start = (currentPage - 1) * itemsPerPage;
        int end = Math.min(currentPage * itemsPerPage, allItems.size());

        // Дополнительная проверка, на случай если что-то пошло не так
        if (start >= allItems.size()) {
            start = Math.max(0, allItems.size() - itemsPerPage); // Берем последний "кусок"
            if(start < 0) {
                start = 0; //Если данных меньше чем itemsPerPage
            }
            currentPage = totalPages;
            updatePageInfo();

        }
        if(start > end){
            end = allItems.size(); //Если start > end, берем до конца списка
        }


        List<Item> currentPageItems = allItems.subList(start, end);
        adapter.setItems(currentPageItems);
        adapter.notifyDataSetChanged();
    }
    private class FetchDataTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... params) {
            String urlStr = params[0];
            //int rowsCount = Integer.parseInt(params[1]);  //Убрал rowsCount
            StringBuilder jsonData = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Чтение ответа
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonData.append(line);
                }
                reader.close();

                jsonString = jsonData.toString();
                // Парсинг JSON с помощью Gson
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Item>>(){}.getType();
                //List<Item> items = gson.fromJson(jsonData.toString(), listType);  //Не использую rowsCount
                allItems = gson.fromJson(jsonData.toString(), listType); // Сохраняем всё

                // Расчет кол-ва страниц
                totalPages = (int) Math.ceil((double) allItems.size() / itemsPerPage);

                return allItems;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            progressBar.setVisibility(View.GONE);
            if (items != null) {
                //adapter.setItems(items); //Убрал, так как использую allItems
                updatePageInfo();
                displayCurrentPage();
                setEnableBtnSave(true);
            } else {
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText("Ошибка загрузки данных");
            }
        }
    }
    private class LoadDataFromFileTask extends AsyncTask<Void, Void, List<Item>> {
        @Override
        protected List<Item> doInBackground(Void... voids) {
            File file = new File(getActivity().getExternalFilesDir(null), "data.txt");
            StringBuilder jsonData = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonData.append(line);
                }

                //Парсим JSON
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Item>>() {}.getType();
                allItems = gson.fromJson(jsonData.toString(), listType);
                totalPages = (int) Math.ceil((double) allItems.size() / itemsPerPage); // Обновляем totalPages
                return allItems;


            } catch (IOException e) {
                Log.e("LoadDataFromFileTask", "Error reading file", e);
                return null;  // Вернем null в случае ошибки
            }
        }
        @Override
        protected void onPostExecute(List<Item> items) {
            progressBar.setVisibility(View.GONE);
            if (items != null) {
                //adapter.setItems(items); //Заменил на displayCurrentPage
                updatePageInfo(); //Обновляем информацию о странице
                displayCurrentPage(); // Отображаем текущую страницу
                setEnableBtnSave(true); //Можем включить, но можно и убрать
            } else {
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText("Ошибка загрузки данных из файла или файл пуст");
            }
        }
    }
}