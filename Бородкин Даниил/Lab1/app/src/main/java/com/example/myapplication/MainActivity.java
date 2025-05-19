package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LayoutInflater inflater;
    CarAdapter adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.car_recycler);
        Button loadDataButton = findViewById(R.id.load_data_button);
        Button labDataButton = findViewById(R.id.lab_data_button);
        inflater = getLayoutInflater();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadDataButton.setOnClickListener(v -> {
                try {
                    JsonRequest.send_request(this, this::updateAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });
        labDataButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Данные о лабораторной работе")
                .setMessage("Сделал: Бородкин Д.В.ПО-10\n" +
                    "Реализовать интерфейс приложения для отображения списка элементов. В качестве данных для списка использовать файл в формате json, загруженный с удаленного сервера. Загрузка выполняется в ходе работы по команде пользователя, например, «Загрузить данные» \n" +
                    "Приложение в минимальном исполнении должно:\n" +
                    "    • отображать список элементов внутри фрагмента\n" +
                    "    • список занимает более одного экрана (прокрутка)\n" +
                    "    • список можно пролистать\n" +
                    "    • отдельный элемент списка с пользовательским стилем/дизайном\n" +
                    "    • выполнять запрос на получение данных с удаленного сервера\n" +
                    "    • выполнять преобразование json-структуры в коллекцию объектов\n" +
                    "    • выделение отдельного элемента списка с отображение детальной информации на отдельном экране\n" +
                    "    • отображать детальную информацию об элементе внутри отдельного фрагмента")
                    .create()
                    .show();

        });
    }

    private void updateAdapter(File file) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                List<Car> carList = CarFileHelper.readCarsFromJsonFile(file);
                adapter =new CarAdapter(inflater,carList);
                recyclerView.setAdapter(adapter);
            }


        });
    }
}
