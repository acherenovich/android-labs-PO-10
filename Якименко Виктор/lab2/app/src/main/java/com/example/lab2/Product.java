package com.example.lab2;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String name;
    private double price;
    private boolean isChecked;
    private int quantity;
    private String imagePath; //  Путь к изображению (URL или путь к файлу в ресурсах)

    // Конструктор с imagePath
    public Product(int id, String name, double price, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isChecked = false;
        this.quantity = 1;
        this.imagePath = imagePath;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        isChecked = in.readByte() != 0;
        quantity = in.readInt();
        imagePath = in.readString(); // Читаем imagePath из Parcel
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getImagePath() { return imagePath; } // Геттер для imagePath
    public void setImagePath(String imagePath) { this.imagePath = imagePath; } //Сеттер

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeInt(quantity);
        dest.writeString(imagePath); // Записываем imagePath в Parcel
    }
}