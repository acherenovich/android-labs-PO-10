package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_student) {
            // Переход к активности с информацией о студенте
            Intent intent = new Intent(this, StudentInfoActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_lab) {
            // Переход к активности с информацией о лабораторной
            Intent intent = new Intent(this, LabInfoActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");

        // Получаем ViewPager2 и TabLayout
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Устанавливаем адаптер
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Связываем ViewPager2 с TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Notes");
                    break;
                case 1:
                    tab.setText("Add");
                    break;
                case 2:
                    tab.setText("Del");
                    break;
                case 3:
                    tab.setText("Update");
                    break;
            }
        }).attach();
    }

    public void updateNotesList() {
        FragmentShow fragmentShow = (FragmentShow) getSupportFragmentManager()
                .findFragmentByTag("f" + 0); // 0 - позиция фрагмента в ViewPager

        if (fragmentShow != null) {
            fragmentShow.loadNotesFromDatabase(); // Вызываем обновление списка
        }
    }




}

