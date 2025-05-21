package com.example.lab22;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private TextView tvSelectedCount;
    private Button btnShowChecked;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        tvSelectedCount = findViewById(R.id.tvSelectedCount);
        btnShowChecked = findViewById(R.id.btnShowChecked);

        // Инициализация списка товаров
        productList = new ArrayList<>();
        productList.add(new Product(1, "Товар 1", 100));
        productList.add(new Product(2, "Товар 2", 200));
        productList.add(new Product(3, "Товар 3", 300));
        productList.add(new Product(4, "Товар 4", 344));
        productList.add(new Product(5, "Товар 5", 743));
        productList.add(new Product(6, "Товар 6", 666));
        productList.add(new Product(7, "Товар 7", 59));
        productList.add(new Product(8, "Товар 8", 1488));
        productList.add(new Product(9, "Товар 9", 108));
        productList.add(new Product(10, "Товар 10", 1348));
        productList.add(new Product(11, "Товар 11", 1000));

        // Настройка адаптера
        adapter = new ProductAdapter(this, productList, (position, isChecked) -> updateSelectedCount());
        listView.setAdapter(adapter);

        // Обновление текста при нажатии на кнопку
        btnShowChecked.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("selectedProducts", new ArrayList<>(getSelectedProducts()));
            startActivity(intent);
        });
    }

    private void updateSelectedCount() {
        int count = getSelectedProducts().size();
        tvSelectedCount.setText("Выбрано: " + count);
    }

    private List<Product> getSelectedProducts() {
        List<Product> selectedProducts = new ArrayList<>();
        for (Product product : productList) {
            if (product.isChecked()) {
                selectedProducts.add(product);
            }
        }
        return selectedProducts;
    }
}