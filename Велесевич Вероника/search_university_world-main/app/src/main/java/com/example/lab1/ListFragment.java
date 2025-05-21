package com.example.lab1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;
    private Button buttonLoadData, buttonSettings, buttonSaveToFile, buttonSendEmail;
    private Spinner spinnerQueryType;
    private ArrayList<University> universityList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progressBar);
        buttonLoadData = view.findViewById(R.id.buttonLoadData);
        buttonSettings = view.findViewById(R.id.buttonSettings);
        buttonSaveToFile = view.findViewById(R.id.buttonSaveToFile);
       // buttonSendEmail = view.findViewById(R.id.buttonSendEmail);
        spinnerQueryType = view.findViewById(R.id.spinnerQueryType);
        universityList = new ArrayList<>();

        // Настройка Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.query_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQueryType.setAdapter(adapter);

        // Обработчик выбора элемента в Spinner
        spinnerQueryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedQuery = parent.getItemAtPosition(position).toString();
                String url = getUrlForQuery(selectedQuery);
                loadJSONFromURL(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Обработчик кнопки "Настройки"
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Обработчик кнопки "Загрузить данные"
        buttonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
                String customUrl = sharedPreferences.getString("serverUrl", "http://universities.hipolabs.com/search?country=Belarus");
                loadJSONFromURL(customUrl);
            }
        });

        // Обработчик кнопки "Сохранить в файл"
        buttonSaveToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFile();
            }
        });

//        // Обработчик кнопки "Отправить по email"
//        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendEmail();
//            }
//        });

        // Обработчик клика на элемент списка
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                University selectedUniversity = universityList.get(position);
                openDetailFragment(selectedUniversity);
            }
        });

        return view;
    }

    private String getUrlForQuery(String query) {
        switch (query) {
            case "Университеты Беларуси":
                return "http://universities.hipolabs.com/search?country=Belarus";
            case "Университеты Китая":
                return "http://universities.hipolabs.com/search?country=China";
            case "Университеты Чехии":
                return "http://universities.hipolabs.com/search?country=Czech Republic";
            case "Университеты Швеции":
                return "http://universities.hipolabs.com/search?country=Sweden";
            case "Университеты Италии":
                return "http://universities.hipolabs.com/search?country=Italy";
            default:
                return "http://universities.hipolabs.com/search?country=Belarus";
        }
    }

    private void loadJSONFromURL(String url) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            universityList.clear();
                            JSONArray jsonArray = new JSONArray(response);

                            // Применение настроек
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
                            int rowCount = sharedPreferences.getInt("rowCount", 10);

                            for (int i = 0; i < jsonArray.length() && i < rowCount; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String country = jsonObject.getString("country");
                                String webPage = jsonObject.getJSONArray("web_pages").getString(0);
                                String domain = jsonObject.getJSONArray("domains").getString(0);
                                universityList.add(new University(name, country, webPage, domain));
                            }

                            CustomAdapter adapter = new CustomAdapter(getContext(), R.layout.row, universityList);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Ошибка при обработке данных", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Ошибка загрузки данных: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void saveDataToFile() {
        try {
            File file = new File(getContext().getFilesDir(), "universities.txt");
            FileWriter writer = new FileWriter(file);
            for (University university : universityList) {
                writer.write(university.getName() + ", " + university.getCountry() + "\n");
            }
            writer.close();
            Toast.makeText(getContext(), "Данные сохранены в файл", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Данные университетов");
        intent.putExtra(Intent.EXTRA_TEXT, getUniversityDataAsString());
        startActivity(Intent.createChooser(intent, "Отправить данные"));
    }

    private String getUniversityDataAsString() {
        StringBuilder data = new StringBuilder();
        for (University university : universityList) {
            data.append(university.getName()).append(", ").append(university.getCountry()).append("\n");
        }
        return data.toString();
    }

    private void openDetailFragment(University university) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", university.getName());
        bundle.putString("country", university.getCountry());
        bundle.putString("webPage", university.getWebPage());
        bundle.putString("domain", university.getDomain());
        detailFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}