package com.example.lab33;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.lab33.adapters.NotesPagesAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewPager);
        NotesPagesAdapter adapter = new NotesPagesAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
