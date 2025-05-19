package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private TextView txtSelectedCount;
    private Button btnShowChecked;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        txtSelectedCount = findViewById(R.id.txtSelectedCount);
        btnShowChecked = findViewById(R.id.btnShowChecked);

        // Заполняем список товаров
        productList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            productList.add(new Product(i, "Товар " + i, i * 100));
        }

        // Передаём в адаптер callback для обновления счетчика
        adapter = new ProductAdapter(this, productList, this::updateSelectedCount);
        listView.setAdapter(adapter);

        // Обработчик кнопки "Показать выбранные товары"
        btnShowChecked.setOnClickListener(v -> {
            ArrayList<Product> selectedProducts = new ArrayList<>();
            for (Product p : productList) {
                if (p.isChecked()) selectedProducts.add(p);
            }

            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra("selectedProducts", selectedProducts);
            startActivityForResult(intent, 1); // Ожидаем обновленный список
        });


        // Обновляем счетчик выбранных товаров
        updateSelectedCount();
    }

    private void updateSelectedCount() {
        int count = 0;
        for (Product p : productList) if (p.isChecked()) count++;
        txtSelectedCount.setText("Выбрано: " + count);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<Product> updatedProducts = (ArrayList<Product>) data.getSerializableExtra("updatedProducts");

            // Обновляем товары в главном списке
            for (Product updated : updatedProducts) {
                for (Product p : productList) {
                    if (p.getId() == updated.getId()) {
                        p.setChecked(updated.isChecked());
                    }
                }
            }

            // Обновляем список и счетчик
            adapter.notifyDataSetChanged();
            updateSelectedCount();
        }
    }

}
