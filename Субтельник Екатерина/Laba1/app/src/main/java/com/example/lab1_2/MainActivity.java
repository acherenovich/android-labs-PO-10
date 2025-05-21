package com.example.lab1_2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private boolean isGridView = false;

    private static final String URL = "http://10.0.2.2:3000/items";

    private String dialogText = "Реализовать интерфейс приложения для отображения списка элементов. В качестве данных для списка использовать файл в формате json, загруженный с удаленного сервера. Загрузка выполняется в ходе работы по команде пользователя, например, «Загрузить данные». Приложение в минимальном исполнении должно:\n" +
            "• отображать список элементов внутри фрагмента\n" +
            "• список занимает более одного экрана (прокрутка)\n" +
            "• список можно пролистать\n" +
            "• отдельный элемент списка с пользовательским стилем/дизайном\n" +
            "• выполнять запрос на получение данных с удаленного сервера\n" +
            "• выполнять преобразование json-структуры в коллекцию объектов\n" +
            "• выделение отдельного элемента списка с отображением детальной информации на отдельном экране\n" +
            "• отображать детальную информацию об элементе внутри отдельного фрагмента\n\n" +
            "Бонусы:\n" +
            "• Присутствие на экране кнопок управления видом отображаемой информации\n" +
            "• Преобразование и сохранение информации запроса (например, в текстовый файл или другой формат CSV, локально или в сеть…)\n" +
            "• Разработка и использование собственного адаптера\n" +
            "• Включение изображений в список\n" +
            "• Обработка исключений с выводом сообщений";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);

        if (isLandscape) {
            fragmentContainer.setVisibility(View.VISIBLE);
        }

        adapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                if (isLandscape) {
                    ProductDetailFragment fragment = ProductDetailFragment.newInstance(
                            product.getName(),
                            product.getDescription(),
                            product.getImageUrl(),
                            product.getPrice()

                    );
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();

                } else {
                    Intent intent = new Intent(MainActivity.this, ProductDetail.class);
                    intent.putExtra("product_name", product.getName());
                    intent.putExtra("product_description", product.getDescription());
                    intent.putExtra("product_image", product.getImageUrl());
                    intent.putExtra("product_price", product.getPrice());
                    intent.putExtra("product_category", product.getCategory());
                    intent.putExtra("product_rating", product.getRating());
                    startActivity(intent);
                }
            }
        }, isGridView);

        recyclerView.setAdapter(adapter);

        Button btnFetch = findViewById(R.id.btn_fetch);
        Button btnToggleView = findViewById(R.id.btn_toggle_view);
        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setVisibility(View.GONE);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productList.isEmpty()){
                    btnFetch.setText("Hide Products");
                    fetchProducts();
                    btnSave.setVisibility(View.VISIBLE);
                } else {
                    productList.clear();
                    adapter.notifyDataSetChanged();
                    btnFetch.setText("Show products");
                    btnSave.setVisibility(View.GONE);
                }
            }
        });

        btnToggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGridView = !isGridView;
                recyclerView.setLayoutManager(isGridView ? new GridLayoutManager(MainActivity.this, 2) : new LinearLayoutManager(MainActivity.this));
                adapter.setGridView(isGridView);
                btnToggleView.setText(isGridView ? "List View" : "Grid View");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFile();
            }
        });

        ImageView ivMoreOptions = findViewById(R.id.iv_more_options);
        ivMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Info")
                        .setMessage(dialogText)
                        .setPositiveButton("OK", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                TextView textView = dialog.findViewById(android.R.id.message);
                if (textView != null) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                }
            }
        });


    }

    private void fetchProducts() {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                Type listType = new TypeToken<List<Product>>() {}.getType();
                                productList.clear();
                                productList.addAll(new Gson().fromJson(response.toString(), listType));
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                Log.e("ParseError", "Error parsing data", e);
                                Toast.makeText(MainActivity.this, "Error processing data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", "Error fetching data", error);
                            Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        }
                    });

            Log.d("MainActivity", "Sending request to server");
            queue.add(request);
        } catch (Exception e) {
            Log.e("NetworkError", "Error creating request", e);
            Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDataToFile() {
        StringBuilder data = new StringBuilder();
        for (Product product : productList) {
            data.append("Name: ").append(product.getName()).append("\n")
                    .append("Category: ").append(product.getCategory()).append("\n")
                    .append("Description: ").append(product.getDescription()).append("\n")
                    .append("Price: ").append(product.getPrice()).append("\n\n");
        }

        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (downloadsFolder.exists() || downloadsFolder.mkdirs()) {
            File file = new File(downloadsFolder, "products.txt");

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data.toString().getBytes());
                Toast.makeText(MainActivity.this, "Data saved successfully to Downloads", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("SaveError", "Error saving data to file", e);
                Toast.makeText(MainActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Cannot access Downloads folder", Toast.LENGTH_SHORT).show();
        }
    }
}
