package com.example.kancergokirmak.facebookloginforandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

/**
 * Created by kancergokirmak on 02/12/15.
 */
public class RegisterActivity extends AppCompatActivity {

    private Button cusRegisterButton;
    private EditText userMail;
    private EditText userPass;
    private TextView reg_info;
    private TextView info2;
    TextView loginScreen;
    private ProfileTracker profileTracker;
    LinearLayout send_mes_lay;
    Firebase FIREBASE_DB;
    private LoginButton faceRegButton;
    private CallbackManager callbackManager_reg;
    private CheckBox cbShowPwd;

    RegisterAccount reg_acc = new RegisterAccount();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager_reg = CallbackManager.Factory.create();

        setContentView(R.layout.register);

        cusRegisterButton = (Button) findViewById(R.id.cus_register);
        loginScreen = (TextView) findViewById(R.id.link_to_login);
        userMail = (EditText) findViewById(R.id.reg_user_mail);
        userPass = (EditText) findViewById(R.id.reg_user_password);
        reg_info = (TextView) findViewById(R.id.reg_info);
        faceRegButton = (LoginButton) findViewById(R.id.face_register_button);
        cbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);

        //login ekraninageri donus yapmayi saglar.
        loginScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

        cusRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //kullanicinin veritabanina kaydedilmesi icin post request yapilir.
                reg_acc.registerAccount(RegisterActivity.this, userMail.getText().toString().trim(), userPass.getText().toString().trim(), RegisterActivity.this, reg_info);
            }
        });

        faceRegButton.registerCallback(callbackManager_reg, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                send_message_start();
            }

            @Override
            public void onCancel() {
                reg_info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                reg_info.setText("Login attempt failed.");
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

        //parolayi acik/gizli girebilmek icin checkbox'i dinleyen bir listener yazdik..
        cbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    //show password
                    userPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    userPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager_reg.onActivityResult(requestCode, resultCode, data);
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
