package com.example.kancergokirmak.facebookloginforandroid;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import model.LoginResponse;

/**
 * Created by kancergokirmak on 02/12/15.
 */
public class RegisterAccount {

    RequestQueue queue;
    String account_post_url = "http://52.27.143.102:8080/register";
    public static final String TAG = "RegisterAccount";

    public void registerAccount(final Context currentContext, final String mail, final String password, final RegisterActivity act, final TextView reg_info) {

        queue = Volley.newRequestQueue(currentContext);

        final Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user_mail", mail);
        jsonParams.put("user_password", password);

        JsonObjectRequest postRequestForReg = new JsonObjectRequest(Request.Method.POST, account_post_url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, response.toString());

                LoginResponse loginResponse = new LoginResponse(response);

                if (loginResponse.getStatus().equals("SUCCESS")){
                    //Toast bas kullanici daha onceden kayitlidir diye ve 2.layoutu calisitir
                    Toast.makeText(currentContext,"Kullanici kayitlidir!",Toast.LENGTH_SHORT).show();
                    act.send_message_start(); // 2.layout baslatilir
                }else if(loginResponse.getStatus().equals("REGISTER")){
                    //Kullanici veritabaninda bulunamadigi icin veritabanina kaydedilir.
                    Toast.makeText(currentContext,"Kullanici sistemimize kaydedilmistir!",Toast.LENGTH_SHORT).show();
                    reg_info.setText("Kullanici kaydedilmistir!");
                    act.send_message_start();
                }else{
                    Toast.makeText(currentContext, "Lütfen bilgilerinizi kontrol ediniz!", Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Handle error
                        Log.i(TAG, "REGISTER ERROR: " + error.toString());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_mail", mail);
                params.put("user_Password", password);
                return super.getParams(); //params;
            }
        };
        queue.add(postRequestForReg);
    }
}