package com.example.lab1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    public static final String SELECTED_ITEM = "SELECTED_ITEM";
    Country selectedItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SelectedCountryHolder.getInstance().setSelectedCountry(null);
                finish(); // Закрываем активность
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        setContentView(R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            selectedItem = (Country) extras.getSerializable(SELECTED_ITEM);
        else {
            // Загружаем ранее выбранный элемент
            Country selectedCountry = SelectedCountryHolder.getInstance().getSelectedCountry();
            if (selectedCountry != null) {
                selectedItem = selectedCountry;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_fragment_container);
        if (fragment != null)
            fragment.setSelectedItem(selectedItem);
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        SelectedCountryHolder.getInstance().setSelectedCountry(null);
//    }

}