package com.example.lab1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private ListView listView;
    private CustomAdapter adapter;
    private List<Item> itemList;
    private Button btnLoadData;
    private EditText etServerUrl;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        listView = rootView.findViewById(R.id.list_view);
        btnLoadData = rootView.findViewById(R.id.btn_load_data);
        etServerUrl = rootView.findViewById(R.id.et_server_url);
        itemList = new ArrayList<>();

        adapter = new CustomAdapter(getActivity(), itemList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = itemList.get(position);
            DetailFragment detailFragment = DetailFragment.newInstance(selectedItem);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnLoadData.setOnClickListener(v -> {

            String url = etServerUrl.getText().toString().trim();
            if(url.isEmpty()){
                url = "https://jsonplaceholder.typicode.com/posts";
            }
            new DownloadDataTask().execute(url);
        });

        return rootView;
    }

    private class DownloadDataTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... urls) {
            String urlString = urls[0];
            List<Item> result = new ArrayList<>();
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder jsonBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }
                    reader.close();
                    String jsonString = jsonBuilder.toString();

                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String description = jsonObject.getString("body");
                        result.add(new Item(id, title, description));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            if (items != null && !items.isEmpty()){
                itemList.clear();
                itemList.addAll(items);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
