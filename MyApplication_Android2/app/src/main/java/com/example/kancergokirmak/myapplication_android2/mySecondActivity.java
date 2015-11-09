package com.example.kancergokirmak.myapplication_android2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

public class mySecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_second);
        Intent myIntent = getIntent();
        TextView secondActivity = (TextView)findViewById(R.id.secondActivity);
        secondActivity.setText(myIntent.getStringExtra(MainActivity.MyIntentKey));
    }
}
