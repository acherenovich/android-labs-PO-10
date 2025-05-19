package com.example.lab3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private Button btnShow, btnAdd, btnDel, btnUpdate;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация ViewPager
        viewPager = findViewById(R.id.viewPager);
        // Создание адаптера для ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentShow(), "Show");
        adapter.addFragment(new FragmentAdd(), "Add");
        adapter.addFragment(new FragmentDel(), "Del");
        adapter.addFragment(new FragmentUpdate(), "Update");
        viewPager.setAdapter(adapter);

        // Инициализация кнопок навигации
        btnShow = findViewById(R.id.btnShow);
        btnAdd = findViewById(R.id.btnAdd);
        btnDel = findViewById(R.id.btnDel);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Обработчики кнопок для переключения между фрагментами
        btnShow.setOnClickListener(v -> viewPager.setCurrentItem(0));
        btnAdd.setOnClickListener(v -> viewPager.setCurrentItem(1));
        btnDel.setOnClickListener(v -> viewPager.setCurrentItem(2));
        btnUpdate.setOnClickListener(v -> viewPager.setCurrentItem(3));
    }
}
