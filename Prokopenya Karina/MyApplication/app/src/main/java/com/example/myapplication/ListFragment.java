
package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Button loadButton;
    private ListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadButton = view.findViewById(R.id.loadButton);

        // Инициализируем ViewModel
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);

        // Подписываемся на изменения данных
        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                // Если данные загружены, обновляем адаптер
                adapter = new ItemAdapter(items, item -> {
                    // Передаем данные в DetailFragment
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, DetailFragment.newInstance(item.getTitle(), item.getDescription(), item.getImageUrl()))
                            .addToBackStack(null)
                            .commit();
                });
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка загружает данные
        loadButton.setOnClickListener(v -> {
            // Загружаем данные при нажатии на кнопку
            viewModel.loadItems();
        });

        return view;
    }
}


