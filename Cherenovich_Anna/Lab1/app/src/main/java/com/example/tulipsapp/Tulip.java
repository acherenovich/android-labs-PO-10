package com.example.tulipsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Tulip implements Parcelable {
    private String name;
    private String color;
    private String height;
    private String imageUrl;

    public Tulip(String name, String color, String height, String imageUrl) {
        this.name = name;
        this.color = color;
        this.height = height;
        this.imageUrl = imageUrl;
    }

    protected Tulip(Parcel in) {
        name = in.readString();
        color = in.readString();
        height = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Tulip> CREATOR = new Creator<Tulip>() {
        @Override
        public Tulip createFromParcel(Parcel in) {
            return new Tulip(in);
        }

        @Override
        public Tulip[] newArray(int size) {
            return new Tulip[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getHeight() {
        return height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(color);
        dest.writeString(height);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
