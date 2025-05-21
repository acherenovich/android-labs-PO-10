package com.example.lab1;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button openDialogButton, getDataButton;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDialogButton = findViewById(R.id.button);
        getDataButton = findViewById(R.id.button2);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        openDialogButton.setOnClickListener(v -> showTextDialog());
        getDataButton.setOnClickListener(v -> fetchData());
    }

    private void showTextDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Информация о студенте")
                .setMessage("Разработал Конашук Никита Валерьевич, студент группы ПО-10. " +
                        "Задание: Реализовать интерфейс приложения для отображения списка элементов. В качестве данных для списка использовать файл в формате json, загруженный с удаленного сервера. Загрузка выполняется в ходе работы по команде пользователя, например, «Загрузить данные» \n" +
                        "Приложение в минимальном исполнении должно:\n" +
                        "    • отображать список элементов внутри фрагмента\n" +
                        "    • список занимает более одного экрана (прокрутка)\n" +
                        "    • список можно пролистать\n" +
                        "    • отдельный элемент списка с пользовательским стилем/дизайном\n" +
                        "    • выполнять запрос на получение данных с удаленного сервера\n" +
                        "    • выполнять преобразование json-структуры в коллекцию объектов\n" +
                        "    • выделение отдельного элемента списка с отображение детальной информации на отдельном экране\n" +
                        "    • отображать детальную информацию об элементе внутри отдельного фрагмента")

                .setPositiveButton("OK", null)
                .show();
    }

    private void fetchData() {
        String url = "http://10.0.2.2:3000/posts";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> showDialog("Ошибка", "Не удалось получить данные."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Post>>(){}.getType();
                    List<Post> posts = gson.fromJson(responseData, listType);

                    runOnUiThread(() -> adapter.setPosts(posts));
                } else {
                    runOnUiThread(() -> showDialog("Ошибка", "Ответ сервера: " + response.code()));
                }
            }
        });
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
