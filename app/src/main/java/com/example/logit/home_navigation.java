package com.example.logit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class home_navigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);
        getSupportActionBar().hide();
    }
}