package com.example.lab2;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String name;
    private double price;
    private boolean isChecked;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isChecked = false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(price);
    }

    public static final Creator<Product> CREATOR = new Parcelable.Creator<Product>(){
        @Override
        public Product createFromParcel(Parcel source) {
            int id = source.readInt();
            String name = source.readString();
            double price = source.readDouble();
            return new Product(id, name, price);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
}
