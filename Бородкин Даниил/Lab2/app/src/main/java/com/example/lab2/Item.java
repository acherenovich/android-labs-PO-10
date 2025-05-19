package com.example.lab2;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable, Comparable<Item> {
    private int id;
    private String name;
    private String price;

    boolean checked;

    public Item(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = String.valueOf(price);
        checked = false;
    }
    private Item(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        price = parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(price);
    }
    public static final Parcelable.Creator<Item> CREATOR = new
            Parcelable.Creator<Item>() {
                // распаковываем объект из Parcel
                public Item createFromParcel(Parcel in) {
                    return new Item(in);
                }
                public Item[] newArray(int size) {
                    return new Item[size];
                }
            };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = String.valueOf(price);
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }
    public boolean getChecked(){
        return checked;
    }

    @Override
    public int compareTo(Item other){
        return Integer.compare(this.id, other.getId());
    }
}
