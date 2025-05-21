package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private TextView checkedCountTextView;
    private Button showCheckedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewProducts = findViewById(R.id.listViewProducts);
        checkedCountTextView = new TextView(this); // Создаем TextView динамически,
        //  он понадобится позже, в футере.
        showCheckedButton = new Button(this);
        showCheckedButton.setText("Show Checked Items");

        // Создаем список товаров (пример)
        productList = new ArrayList<>();
        productList.add(new Product(1, "Телефон Apple iPhone 16 128GB (ультрамарин)", 3399.0, "https://imgproxy.onliner.by/mjfEqdvGDvu4pQ8awFBT4Fox44cMNhYKV_O_5HiRc1I/w:176/h:176/z:2/f:jpg/aHR0cHM6Ly9jb250/ZW50Lm9ubGluZXIu/YnkvY2F0YWxvZy9k/ZXZpY2Uvb3JpZ2lu/YWwvNjVhYTJkNGYx/ZDBjNjRiNjQ1OGQ2/ZGUyOWFmM2IwMDku/anBn")); // product1.png в res/drawable
        productList.add(new Product(2, "Телефон Apple iPhone 15 128GB (черный)", 2799.0, "https://imgproxy.onliner.by/vijQOh4PIg8ophRbFoCJtCfNGb9oos76pIC2yLIosz8/w:176/h:176/z:2/f:jpg/aHR0cHM6Ly9jb250/ZW50Lm9ubGluZXIu/YnkvY2F0YWxvZy9k/ZXZpY2Uvb3JpZ2lu/YWwvZjI3N2E5Y2Uz/ZjQ5YWY5MTgzOGQ4/YzEwNDQxYjRmNDYu/anBn")); // product2.png в res/drawable
        productList.add(new Product(3, "Телефон Apple iPhone 15 Pro Max 256GB (природный титан)", 4599.0, "https://imgproxy.onliner.by/sRbjYLBpuHFFLcrJP3AczOBYP-NogtsuI0pJpHqH0ak/w:176/h:176/z:2/f:jpg/aHR0cHM6Ly9jb250/ZW50Lm9ubGluZXIu/YnkvY2F0YWxvZy9k/ZXZpY2Uvb3JpZ2lu/YWwvY2U4MDNhZjNj/ODkyNTZiYTU1NTlm/ZmVkMWI4YzNjZDku/anBn"));


        // Добавляем Header
        View header = getLayoutInflater().inflate(R.layout.list_header, null, false);
        listViewProducts.addHeaderView(header);

        // Добавляем Footer (с TextView и Button)
        View footerView = getLayoutInflater().inflate(R.layout.list_footer, null, false);
        checkedCountTextView = footerView.findViewById(R.id.textViewCheckedCount);
        showCheckedButton = footerView.findViewById(R.id.buttonShowChecked);
        listViewProducts.addFooterView(footerView);

        // Создаем адаптер и связываем его с ListView
        productAdapter = new ProductAdapter(this, productList, checkedCountTextView);
        listViewProducts.setAdapter(productAdapter);


        // Обработчик нажатия на кнопку "Show Checked Items"
        showCheckedButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            ArrayList<Product> checkedProducts = (ArrayList<Product>) productAdapter.getCheckedProducts();
            intent.putExtra("checkedProducts", checkedProducts);
            startActivity(intent);
        });
    }
}