package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public final static int MAX_LENGTH_COMPUTATION_QUEUE = 7;
    private MyClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        listener = MyClickListener.getInstance(this);
    }

    public void clickButton(View v) {
        listener.onClick(v);
    }
}