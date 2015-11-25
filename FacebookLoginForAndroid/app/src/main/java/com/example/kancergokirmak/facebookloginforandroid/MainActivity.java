package com.example.kancergokirmak.facebookloginforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    Firebase FIREBASE_DB;
    private TextView info;
    private LoginButton loginButton;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    LinearLayout send_mes_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.content_main);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
               send_message_start(); //yukaridaki info alani diger layout da bos oldugu icin ekranda gorulmeyecektir.
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

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if(currentProfile == null){
                    send_mes_lay.removeAllViews();
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void send_message_start(){
        super.onStart();

        setContentView(R.layout.send_message_main);

        // logout olduktan sonra mesajlasma layoutunu kaldirmak icin kullanilacak.
        send_mes_lay = (LinearLayout) findViewById(R.id.send_mes_layout_main);

        Firebase.setAndroidContext(this);

        FIREBASE_DB = new Firebase("https://flickering-fire-4607.firebaseio.com");

        final TextView ViewText = (TextView)findViewById(R.id.ViewText);
        FIREBASE_DB.child("messanger_group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newDataMessage = (String) dataSnapshot.child("UserMessage").getValue();
                String newDataUsername = (String) dataSnapshot.child("UserName").getValue();
                ViewText.setText(newDataUsername + " says: \n" + newDataMessage);
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

        FIREBASE_DB.child("messanger_group").child("UserMessage").setValue(message);
        FIREBASE_DB.child("messanger_group").child("UserName").setValue(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}