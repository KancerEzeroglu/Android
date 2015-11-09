package com.example.kancergokirmak.myapplication_android2;

import android.content.Intent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public final static String MyIntentKey = "MyTextBoxValue";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void btnOK_Click(View currentView){
        Intent myIntent = new Intent(this,mySecondActivity.class); // ikinci bir activity baslatiliyor
        EditText myTextBox = (EditText)findViewById(R.id.txtMyText);
        String myValue = myTextBox.getText().toString();
        myIntent.putExtra(MyIntentKey,myValue);
        startActivity(myIntent);
    }
}
