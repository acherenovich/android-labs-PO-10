package com.example.lab1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ItemListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private static final String JSON_URL = "https://raw.githubusercontent.com/Dan4kk777/json-data-repo/main/data.json";

    private static final int ITEMS_PER_PAGE = 10;
    private int currentPage = 1;
    private List<Item> allItems;
    private TextView pageNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button loadButton = view.findViewById(R.id.load_button);
        Button prevButton = view.findViewById(R.id.prev_page_button);
        Button nextButton = view.findViewById(R.id.next_page_button);
        Button settingsButton = view.findViewById(R.id.settings_button);
        pageNumber = view.findViewById(R.id.page_number);

        loadButton.setOnClickListener(v -> new FetchDataTask().execute(JSON_URL));

        prevButton.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                updatePage();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentPage * ITEMS_PER_PAGE < allItems.size()) {
                currentPage++;
                updatePage();
            }
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void updatePage() {
        if (allItems == null || allItems.isEmpty()) return;

        int start = (currentPage - 1) * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, allItems.size());

        List<Item> pageItems = allItems.subList(start, end);
        adapter = new ItemAdapter(getContext(), pageItems, item -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ItemDetailFragment.newInstance(item))
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);
        pageNumber.setText("Страница: " + currentPage);
    }

    private class FetchDataTask extends AsyncTask<String, Void, List<Item>> {
        @Override
        protected List<Item> doInBackground(String... strings) {
            String urlString = strings[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    System.out.println("Ошибка HTTP: " + responseCode);
                    return null;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                Gson gson = new Gson();
                List<Item> fetchedItems = gson.fromJson(reader, new TypeToken<List<Item>>() {}.getType());
                reader.close();

                if (fetchedItems == null) {
                    System.out.println("Ошибка: JSON загружен, но парсинг вернул null.");
                } else {
                    System.out.println("Данные загружены: " + fetchedItems.size() + " элементов.");
                }
                return fetchedItems;

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ошибка загрузки данных: " + e.getMessage());
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Item> items) {
            if (items == null || items.isEmpty()) {
                Toast.makeText(getContext(), "Ошибка загрузки данных. Проверь URL.", Toast.LENGTH_SHORT).show();
                return;
            }
            allItems = items;
            currentPage = 1;
            updatePage();
        }
    }
}
