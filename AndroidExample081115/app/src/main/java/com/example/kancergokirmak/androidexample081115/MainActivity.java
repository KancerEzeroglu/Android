package com.example.kancergokirmak.androidexample081115;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private TextView myStatusLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myStatusLabel=(TextView)findViewById(R.id.lblStatus);
        myStatusLabel.setText("On create called");
        Log.i(MainActivity.class.toString(),"On Create called");
    }

    @Override
    protected void onStart(){
        super.onStart(); // oncelikle super class cagirilmalidir
        myStatusLabel.append("\n On Start Called");
        Log.i(MainActivity.class.toString(),"On Start Called");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        myStatusLabel.append("\n On Restart Called");
        Log.i(MainActivity.class.toString(),"On Restart Called");
    }

    @Override
    protected void onPause(){
        super.onPause();
        myStatusLabel.append("\n On pause called");
        Log.i(MainActivity.class.toString(),"On Pause Called");
    }

    @Override
    protected void onStop(){
        super.onStop();
        myStatusLabel.append("\n On Stop Called");
        Log.i(MainActivity.class.toString(),"On Stop Called");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        myStatusLabel.append("\n OnDestroy Called");
        Log.i(MainActivity.class.toString(),"On Destroy Called");
    }

    public void btnSendSMS_Click(View v){
        Intent getPhoneData = new Intent(Intent.ACTION_PICK); // disariya bir istek gonderecegim diyoruz Action_Pick ile.
        getPhoneData.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        if(getPhoneData.resolveActivity(getPackageManager()) != null){ // bir activity var mi diye bakiyoruz.
            startActivityForResult(getPhoneData, 1); //burada contacts aciliyor.
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ // activiyden herhangi bir sey dondugunde burasi calisir.
        Toast.makeText(getBaseContext(),"ContactList Açılacak!",5000).show();
        if (requestCode == 1 && resultCode == RESULT_OK){
            Uri contactData = data.getData();
            String[] phoneNumberData = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}; // contact list'in tipi burada. Number bu tipte saklaniyor.
            Cursor cursor = getContentResolver().query(contactData, phoneNumberData, null, null, null);// cusor sql'deki gibiymis.contact Data icerisindeki phoneNumber datasini al.
            //if the cursor is returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()){
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phoneNumber = cursor.getString(numberIndex); // burada numara geliyor.

                Intent numberDialer = new Intent(Intent.ACTION_CALL); // intent, UI'si olmayan activity gibi dusunebiliriz.
                numberDialer.setData(Uri.parse("tel:"+phoneNumber));
                if(numberDialer.resolveActivity(getPackageManager()) != null){
                    startActivity(numberDialer);
                }

                /*Intent intent = new Intent(Intent.ACTION_SENDTO);//ACTION_SENDTO sms gondermek icin kullaniliyor.
                intent.setData(Uri.parse("smsto:"+phoneNumber)); // smsto ile default sms uygulamasi acilir
                intent.putExtra("sms_body","Android ile ilk sms'im");
                if (intent.resolveActivity(getPackageManager()) != null){ // bir sms uygulamasi var mi diye kontrol ediliyor.
                    startActivity(intent);
                }*/
            }
        }
    }
}
