package com.example.kancergokirmak.myfirebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Firebase FIREBASE_DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        FIREBASE_DB = new Firebase("https://intense-heat-8641.firebaseio.com/");
    }

    @Override
    protected void onStart(){
        super.onStart();

        final TextView ViewText = (TextView)findViewById(R.id.ViewText);
        FIREBASE_DB.child("db_grubu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newDataMessage = (String) dataSnapshot.child("kullanici_msg").getValue();
                String newDataUsername = (String) dataSnapshot.child("kullanici_adi").getValue();
                ViewText.setText(newDataUsername + "diyor ki: \n" + newDataMessage);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public void send_message(View view){
        EditText editText = (EditText)findViewById(R.id.editText);
        EditText editUser = (EditText)findViewById(R.id.usertext);

        String message = editText.getText().toString();
        String username = editUser.getText().toString();

        FIREBASE_DB.child("db_grubu").child("kullanici_msg").setValue(message);
        FIREBASE_DB.child("db_grubu").child("kullanici_adi").setValue(username);
    }
}
