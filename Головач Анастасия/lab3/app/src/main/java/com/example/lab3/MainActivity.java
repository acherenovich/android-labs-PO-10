package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private DatabaseAdapter adapter;
    ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Отключаем стандартный заголовок
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Устанавливаем кастомный заголовок
        View customView = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
        getSupportActionBar().setCustomView(customView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // Получаем ViewModel
        NotesViewModel notesViewModel = new ViewModelProvider(this, new NotesViewModelFactory(this)).get(NotesViewModel.class);
        adapter = notesViewModel.getDatabaseAdapter();

        pager = findViewById(R.id.pager);
        FragmentStateAdapter pageAdapter = new PageAdapter(this);
        pager.setAdapter(pageAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator= new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy(){

            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                switch (position) {
                    case PageAdapter.SHOW_PAGE:
                        tab.setText("Заметки");
                        break;
                    case PageAdapter.ADD_PAGE:
                        tab.setText("Добавить");
                        break;
                    case PageAdapter.DEL_PAGE:
                        tab.setText("Удалить");
                        break;
                    case PageAdapter.UPDATE_PAGE:
                        tab.setText("Обновить");
                        break;
                }

            }
        });
        tabLayoutMediator.attach();
    }

    public void switchToPage(int page) {
        ViewPager2 pager = findViewById(R.id.pager);
        pager.setCurrentItem(page, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.labinfo_settings) {
            Intent intent = new Intent(this, LabinfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}