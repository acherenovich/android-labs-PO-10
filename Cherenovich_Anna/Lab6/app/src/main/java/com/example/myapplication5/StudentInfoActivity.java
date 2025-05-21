package com.example.myapplication5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class StudentInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        getSupportActionBar().setTitle("Выполнила Черенович Анна");

    }
}
