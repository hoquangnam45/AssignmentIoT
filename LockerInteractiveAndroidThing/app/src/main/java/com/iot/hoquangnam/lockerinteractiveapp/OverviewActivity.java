package com.iot.hoquangnam.lockerinteractiveapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OverviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        getSupportActionBar().setTitle("Tổng quan");
    }
}
