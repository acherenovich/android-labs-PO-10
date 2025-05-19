package com.example.lab1;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class DetailFragment extends Fragment {
    private TextView countryNameTextView, capitalTextView, populationTextView, languageTextView, currencyTextView;
    private ImageView flagImageView;
    private CardView countryCard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countryNameTextView = getView().findViewById(R.id.countryNameTextView);
        capitalTextView = getView().findViewById(R.id.capitalTextView);
        populationTextView = getView().findViewById(R.id.populationTextView);
        languageTextView = getView().findViewById(R.id.languageTextView);
        currencyTextView = getView().findViewById(R.id.currencyTextView);
        flagImageView = getView().findViewById(R.id.flagImageView);
        countryCard = getView().findViewById(R.id.countryCard);

        // Получаем данные из глобального хранилища ??????????????????????????????
        Country selectedCountry = SelectedCountryHolder.getInstance().getSelectedCountry();

        if (selectedCountry != null) {
            setSelectedItem(selectedCountry);
        }
    }


    // обновление текстового поля
    public void setSelectedItem(Country country) {
        countryCard.setVisibility(View.VISIBLE);

        countryNameTextView.setText(country.getName());
        capitalTextView.setText("Столица: " + country.getCapital());
        populationTextView.setText("Население: " + country.getPopulation() + " млн");
        languageTextView.setText("Язык: " + country.getLanguage());
        currencyTextView.setText("Валюта: " + country.getCurrency());

        Bitmap cachedImage = ImageCache.get(country.getFlagUrl());
        if (cachedImage != null) {
            flagImageView.setImageBitmap(cachedImage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Получаем настройку для числа строк (например, из SharedPreferences)
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        Set<String> visibleFields = sharedPreferences.getStringSet("visible_fields", new HashSet<>());

        if (!visibleFields.contains("capital")) {
            capitalTextView.setVisibility(View.GONE);
        } else {
            capitalTextView.setVisibility(View.VISIBLE);
        }

        if (!visibleFields.contains("population")) {
            populationTextView.setVisibility(View.GONE);
        } else {
            populationTextView.setVisibility(View.VISIBLE);
        }

        if (!visibleFields.contains("language")) {
            languageTextView.setVisibility(View.GONE);
        } else {
            languageTextView.setVisibility(View.VISIBLE);
        }

        if (!visibleFields.contains("currency")) {
            currencyTextView.setVisibility(View.GONE);
        } else {
            currencyTextView.setVisibility(View.VISIBLE);
        }

    }

}
