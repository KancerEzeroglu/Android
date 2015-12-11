package com.example.kancergokirmak.facebookloginforandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kancergokirmak on 04/12/15.
 */
public class ListViewActivity extends Activity {

    public static final String TAG = "ListViewActivity";
    private ListView mainListView;
    private ListView messagingListView;
    private ArrayAdapter<String> listAdapter;
    RequestQueue get_queue;
    Firebase FIREBASE_DB;
    String mail;
    String itemValue;
    JSONObject tempmessage;
    EditText editText;
    String message;
    private ChatAdapter listMesAdapter;
    Button sendButton;
    int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //chat yapilan kullanicilari listelemek icin
        setContentView(R.layout.listview_main);
        mainListView = (ListView) findViewById(R.id.mainListView);

        ArrayList<String> chatsList = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, R.layout.listview_row, chatsList);

        get_queue = Volley.newRequestQueue(ListViewActivity.this);

        //AccountGeneratorCheck sinifindan gerekli olan parametreyi alabilmek icin
        Bundle bundle = getIntent().getExtras();

        mail = bundle.getString("get_mail");

        String account_get_url = String.format("http://52.27.143.102:8080/getchats?user_name=%1$s", mail);

        final JsonObjectRequest getReqForChatUsers = new JsonObjectRequest(Request.Method.GET, account_get_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray username_arr = response.getJSONArray("userName");

                    for (int i = 0; i < username_arr.length(); i++) {
                        listAdapter.add(username_arr.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Hata getChatsFromDb'de.");
            }

        });
        get_queue.add(getReqForChatUsers);
        mainListView.setAdapter(listAdapter);

        //listedeki herhangi bir item'a tiklanildiginda asagidaki listener cevap verir.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int itemPosition = position;
                itemValue = (String) mainListView.getItemAtPosition(position);

                //clickleme yapilan isim ile olan mailler mongo'dan cekilmeli. Ve mesajlasma yapabilecegi firebase ekrani gelmeli.!!!!!!

                //iki kullanicinin konusmalari mongodb'den cekiliyor.
                get_messages_DB(); //ekrana daha onceki mesajlari yazdirmak icin kullanilir.

                Toast.makeText(ListViewActivity.this, "Position: " + itemPosition + " ListItem: " + itemValue, Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void get_messages_DB() {
        super.onStart();

        //konusmaya tiklaninca mesajlari gostermek icin
        setContentView(R.layout.messaging_layout);
        messagingListView = (ListView) findViewById(R.id.messagingListView);

        //mesajlari almak icin
        ArrayList<String> mesList = new ArrayList<String>();
        listMesAdapter = new ChatAdapter(ListViewActivity.this, R.layout.messaging_user_layout);

        //buton tiklamasini dinlemek icin kullaniliyor
        sendButton = (Button) findViewById(R.id.btSend);

        final SaveMessages saveMessages = new SaveMessages();

        String get_users_chats_url = String.format("http://52.27.143.102:8080/getMessages?user_1=%1$s&user_2=%2$s", mail, itemValue);

        Log.i("GET Request URL: ", get_users_chats_url);

        final JsonObjectRequest getReqForMessages = new JsonObjectRequest(Request.Method.GET, get_users_chats_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "Mesajlar icin get request yapıldı.");

                try {
                    JSONArray messages_arr = response.getJSONArray("messages_text");

                    for (int i = 0; i < messages_arr.length(); i++) {
                        tempmessage = ((JSONObject) messages_arr.get(i));
                        boolean isOnLeft = tempmessage.getString("user").equals(mail);
                        listMesAdapter.add(new ChatMessage(isOnLeft, tempmessage.getString("content")));//donen json objesinden mesaj icerigini aliyoruz.

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //firebase tanimlamalari
                Firebase.setAndroidContext(ListViewActivity.this);

                FIREBASE_DB = new Firebase("https://flickering-fire-4607.firebaseio.com");

                FIREBASE_DB.child("messanger_group").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String newDataMessage = (String) dataSnapshot.child("UserMessage").getValue();
                        //Yazıyı ben mi yazdim karsisi miyi test et!!

                        //Ekranimizdaki list alanina yazilan veri ekleniyor.
                        listMesAdapter.add(new ChatMessage(true, newDataMessage));

                        //Firebase ilk acildiginda tetikleniyor. Bu nedenle database'e iki kere kayit olmasin diye asagidaki if-else'i koydum.
                        //Onclick ile butonun tiklanmasini dinleyince Firebase'in onDataChange fonksiyonu calismiyor.
                        if (count != 0) {
                            saveMessages.saveMessagesToDB(ListViewActivity.this, mail, itemValue, message, "151210"); //Eger ben mesaj attiysam. Post request yapip db'ye kaydedecegiz. Gerekli parametreleri gonderdik.
                            count++;

                        } else {
                            count++;
                        }
                    }


                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e(TAG, "Canceled from Firebase DB !");
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        get_queue.add(getReqForMessages);
        messagingListView.setAdapter(listMesAdapter);

    }

    public void send_message(View view) {
        editText = (EditText) findViewById(R.id.etMessage);

        message = editText.getText().toString();

        FIREBASE_DB.child("messanger_group").child("UserMessage").setValue(message);
        FIREBASE_DB.child("messanger_group").child("UserName").setValue(mail);

    }
}
