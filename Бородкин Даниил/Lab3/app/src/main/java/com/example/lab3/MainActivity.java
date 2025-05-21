package com.example.lab3;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.example.lab3.adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.example.lab3.database.HttpDatabaseClient;

public class MainActivity extends AppCompatActivity {

    private FragmentStateAdapter adapter;
    private static final String TAG = "MainActivity";
    public static final String DB_FILE_PATH = "/storage/emulated/0/Documents/notes_app.db"; // Путь к базе данных на устройстве
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Скачиваем последнюю версию базы данных при запуске приложения
        downloadDatabase();
        Button labDataButton = findViewById(R.id.load_data_button);
        ViewPager2 pager = findViewById(R.id.main_pager);
        adapter = new PagerAdapter(this);
        pager.setAdapter(adapter);

        String[] tab_titles = {"Показать", "Добавить", "Удалить", "Обновить"};

        TabLayout tabLayout = findViewById(R.id.main_tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                tab.setText(tab_titles[position]);
            }
        });
        tabLayoutMediator.attach();
        labDataButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Данные о лабораторной работе")
                    .setMessage("Сделал: Бородкин Д.В.ПО-10\n" +
                            "1. Разработать приложение MyNotes, представляющее собой View Pager.\n" +
                            "2. Поместить в View Pager четыре фрагмента: FragmentShow, FragmentAdd, FragmentDel, FragmentUpdate.\n" +
                            "3. В View Pager добавить верхнее меню вкладок (PagerTabStrip) с заголовками Show, Add, Del, Update.\n" +
                            "4. Во фрагменте FragmentShow реализовать кастомизированный список заметок ListView с помощью собственного адаптера.\n" +
                            "5. В каждом пункте списка отобразить следующую информацию о заметке пользователя: номер, описание заметки.\n" +
                            "6. Хранение, а также предоставление информации о заметках адаптеру реализовать с помощью базы данных SQLite.\n" +
                            "7. Во фрагменте FragmentAdd реализовать функционал добавления новой заметки посредством ввода описания заметки в поле EditText и добавления информации в базу данных SQLite по нажатию кнопки Add.\n" +
                            "8. Во фрагменте FragmentDel реализовать функционал удаления заметки посредством ввода ее номера в поле EditText и удаления информации из базы данных SQLite по нажатию кнопки Del.\n" +
                            "9. Во фрагменте FragmentUpdate реализовать функционал обновления существующей заметки посредством ввода ее номера в поле EditText, ввода нового описания в поле EditText и обновления информации в базе данных SQLite по нажатию кнопки Update.\n" +
                            "10. База данных, содержащая менее 20 записей, будет считаться отсутствующей.")
                    .create()
                    .show();


        });
    }

    private void downloadDatabase() {
        HttpDatabaseClient.downloadDatabase(DB_FILE_PATH);
    }

}
