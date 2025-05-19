package com.example.lab1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    interface OnFragmentSendDataListener {
        void onSendData(Country data);
    }

    private OnFragmentSendDataListener fragmentSendDataListener;
    private RecyclerView recyclerView;
    private CountryViewModel viewModel;
    private CountryAdapter countryAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Получаем доступ к SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Настроим слушатель для отслеживания изменений в настройках
        listener = (sharedPreferences, key) -> {
            if ("row_count".equals(key)) {
                viewModel.updatePageList();
            }
        };

        // Регистрируем слушатель
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);

        CountryAdapter.OnItemClickListener countryClickListener = new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Country country) {
                // Посылаем данные Activity
                fragmentSendDataListener.onSendData(country);
            }
        };

        // Создаем адаптер только один раз
        if (countryAdapter == null) {
            countryAdapter = new CountryAdapter(requireContext(), new ArrayList<>(), countryClickListener);
            recyclerView.setAdapter(countryAdapter);
        }

        // Получаем ViewModel
        viewModel = new ViewModelProvider(requireActivity(), new CountryViewModelFactory(requireActivity().getApplicationContext())).get(CountryViewModel.class);

        // Наблюдаем за изменением данных
        viewModel.getCountryList().observe(getViewLifecycleOwner(), countries -> {

            // Обновляем данные в адаптере
            countryAdapter.updateData(countries);
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//    }
}
