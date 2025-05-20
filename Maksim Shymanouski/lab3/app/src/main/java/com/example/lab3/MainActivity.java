package com.example.lab3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerTabStrip;

import com.example.lab3.adapters.MyPagerAdapter;


public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    PagerTabStrip tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabStrip = findViewById(R.id.pagerTabStrip);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
