package com.example.myapplication1;

import android.os.Parcel;
import android.os.Parcelable;

class Product implements Parcelable {
    private int id;
    private String name;
    private int price;
    private boolean checked;

    public Product(int id, String name, int price, boolean checked) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.checked = checked;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readInt();
        checked = in.readByte() != 0;
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
    public int getPrice() { return price; }
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeByte((byte) (checked ? 1 : 0));
    }
}
