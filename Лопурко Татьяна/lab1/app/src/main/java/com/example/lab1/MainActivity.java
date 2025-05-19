package com.example.lab1;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;


public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentSendDataListener {

    private CountryViewModel viewModel;
    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNext = findViewById(R.id.btnNext);
        Button btnPrev = findViewById(R.id.btnPrev);

        // Находим и устанавливаем Toolbar как ActionBar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Лопурко Т. Ю.");
//        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleStyle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Отключаем стандартный заголовок
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Устанавливаем кастомный заголовок
        View customView = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
        getSupportActionBar().setCustomView(customView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        btnDownload = findViewById(R.id.btnDownload);
        viewModel = new ViewModelProvider(this, new CountryViewModelFactory(getApplicationContext())).get(CountryViewModel.class);

        btnNext.setOnClickListener(v -> viewModel.nextPage());
        btnPrev.setOnClickListener(v -> viewModel.prevPage());

        viewModel.getIsNextEnabled().observe(this, isEnabled -> btnNext.setEnabled(isEnabled));
        viewModel.getIsPrevEnabled().observe(this, isEnabled -> btnPrev.setEnabled(isEnabled));

        // Если переходим из альбомного режима в портретный и элемент был выбран, открываем DetailActivity
        Country selectedCountry = SelectedCountryHolder.getInstance().getSelectedCountry();
        if (selectedCountry != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSendData(Country country) {

        SelectedCountryHolder.getInstance().setSelectedCountry(country);

        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_fragment_container);
        if (fragment != null && fragment.isVisible())
            fragment.setSelectedItem(country);
        else {
            Intent intent = new Intent(this,
                    DetailActivity.class);
            intent.putExtra(DetailActivity.SELECTED_ITEM, country);
            startActivity(intent);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String serverUrl = prefs.getString("server_url", "https://raw.githubusercontent.com/tatiana262/Android-lab1/main");
        btnDownload.setOnClickListener(viewBtn -> viewModel.loadCountries(serverUrl));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.labinfo_settings) {
            Intent intent = new Intent(this, LabinfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}