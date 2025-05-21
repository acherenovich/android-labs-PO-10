package com.example.lab2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductsViewModel extends ViewModel {
    MutableLiveData<ArrayList<Product>> products = new MutableLiveData<>();

    public LiveData<ArrayList<Product>> getProducts() {
        return products;
    }

    public void generateProducts() {
        if (products.getValue() == null || products.getValue().isEmpty()) {
            ArrayList<Product> generatedProducts = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                generatedProducts.add(new Product(i, "Товар " + i, Math.random() * 100));
            }
            products.setValue(generatedProducts);
        }
    }
}
