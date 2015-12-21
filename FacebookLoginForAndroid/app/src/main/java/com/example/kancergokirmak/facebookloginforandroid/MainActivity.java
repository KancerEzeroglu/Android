package com.example.kancergokirmak.facebookloginforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements Serializable {

    Firebase FIREBASE_DB;
    private TextView info;
    private TextView info2;
    private LoginButton loginButton;
    private Button cusLoginButton;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    LinearLayout send_mes_lay;
    TextView registerScreen;

    AccountGeneratorCheck agc = new AccountGeneratorCheck(); // bu sinif icersindeki fonksiyonu cagirabilmek icin yeni bir nesne yarattik.

    private EditText userMail;
    private EditText userPass;
    //bunun icerisindeki fonksiyonu normal login olmak isteyen biri ilgili butona tiklayinca cagirmak icin onclickListener yazildi.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.content_main);
        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.face_login_button);
        cusLoginButton = (Button) findViewById(R.id.cus_login);
        userMail = (EditText) findViewById(R.id.user_mail);
        userPass = (EditText) findViewById(R.id.user_password);
        registerScreen = (TextView) findViewById(R.id.link_to_register);

        //login butonunu dinler
        cusLoginButton.setOnClickListener(new View.OnClickListener() { //normal login olmak isteyen biri ilgili butona tiklayinca calistirmamiz gereken fonksiyon calistiriliyor.
            @Override
            public void onClick(View v) {

                agc.checkMemberAccount(MainActivity.this, userMail.getText().toString().trim(), userPass.getText().toString().trim(), MainActivity.this, info); // post request  yapar.
            }
        });

        //register linkini dinler ve ikinci activity'i baslatir.
        registerScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                send_message_start();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });


        //kullanici logout olduktan sonra mesajlasma ekranini gormesini engellemek
        //icin kullanildi.
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile == null) {
                    send_mes_lay = (LinearLayout) findViewById(R.id.send_mes_layout_main);
                    send_mes_lay.removeViews(0, 5); // sadece login butonu gorunuyor bu sayede
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void send_message_start() {
        super.onStart();

        setContentView(R.layout.send_message_main);
        info2 = (TextView) findViewById(R.id.info2);

        // kullanici logout olduktan sonra mesajlasma layoutunu kaldirmak icin kullanilacak.
        send_mes_lay = (LinearLayout) findViewById(R.id.send_mes_layout_main);

        Firebase.setAndroidContext(this);

        FIREBASE_DB = new Firebase("https://flickering-fire-4607.firebaseio.com");

        final TextView ViewText = (TextView) findViewById(R.id.ViewText);
        FIREBASE_DB.child("messanger_group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newDataMessage = (String) dataSnapshot.child("UserMessage").getValue();
                String newDataUsername = (String) dataSnapshot.child("UserName").getValue();
                ViewText.setText(newDataUsername + " says: \n" + newDataMessage);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                info2.setText("Canceled from Firebase!");
            }
        });
    }

    public void send_message(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editUser = (EditText) findViewById(R.id.usertext);

        String message = editText.getText().toString();
        String username = editUser.getText().toString();

        FIREBASE_DB.child("messanger_group").child("UserMessage").setValue(message);
        FIREBASE_DB.child("messanger_group").child("UserName").setValue(username);
    }

}
