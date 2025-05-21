package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnSelectionChangedListener {

    ArrayList<Product> products = new ArrayList<Product>();
    ListView productsList;
    ProductAdapter productAdapter;
    TextView selection;

    private TextView tvSelectedCount;
    private View view_header, view_footer;
    private LayoutInflater layoutInflater;
    private Button btnShowCart;
    private int selectedProductsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsList = findViewById(R.id.productsList);

        layoutInflater = LayoutInflater.from(this);
        view_header = layoutInflater.inflate(R.layout.header, null);
        view_footer = layoutInflater.inflate(R.layout.footer, null);
        productsList.addHeaderView(view_header);
        productsList.addFooterView(view_footer);

        tvSelectedCount = findViewById(R.id.tvSelectedCount);
        btnShowCart = findViewById(R.id.btnShowCart);

        // Устанавливаем заголовок
        //getSupportActionBar().setTitle("Лопурко Т. Ю. ПО-10");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Отключаем стандартный заголовок
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Устанавливаем кастомный заголовок
        View customView = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
        getSupportActionBar().setCustomView(customView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        ProductsViewModel model = new ViewModelProvider(this).get(ProductsViewModel.class);
        model.getProducts().observe(this, prs -> {
            products.clear();
            products.addAll(prs);
            productAdapter.notifyDataSetChanged();
        });

        model.generateProducts();

        // получаем элемент ListView
        productsList = findViewById(R.id.productsList);
        //selection = findViewById(R.id.selection);
        productAdapter = new ProductAdapter(this, R.layout.list_item, products);



        productsList.setAdapter(productAdapter);


        // Обработчик нажатия на кнопку "Корзина"
        btnShowCart.setOnClickListener(v -> {
            ArrayList<Product> selectedProducts = new ArrayList<>();
            for (Product product : products) {
                if (product.isChecked()) {
                    selectedProducts.add(product);
                }
            }

            // Передаем выбранные товары в CartActivity
            Intent intent = new Intent(this, CartActivity.class);
            intent.putParcelableArrayListExtra("selected_products", selectedProducts);
            startActivity(intent);
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Product> selectedProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.isChecked()) {
                selectedProducts.add(product);
            }
        }
        outState.putParcelableArrayList("selected_products", selectedProducts);
        outState.putInt("selected_products_count", selectedProductsCount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            selectedProductsCount = savedInstanceState.getInt("selected_products_count");
            ArrayList<Product> selectedProducts = savedInstanceState.getParcelableArrayList("selected_products");
            if (selectedProducts != null) {
                // Восстанавливаем отметки на основе id товаров
                for (Product selected : selectedProducts) {
                    for (Product product : products) {
                        if (product.getId() == selected.getId()) {
                            product.setChecked(true);
                            break;
                        }
                    }
                }

            }
        }
        tvSelectedCount.setText("Выбрано: " + selectedProductsCount);
    }

    @Override
    public void onSelectionChanged(int selectedCount) {
        tvSelectedCount.setText("Выбрано: " + selectedCount);
        selectedProductsCount = selectedCount;
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