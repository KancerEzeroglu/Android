package com.example.kancergokirmak.facebookloginforandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    RequestQueue queue, queue_confirm;
    String account_post_url = "http://52.27.143.102:8080/register";
    // String account_post_url = "http://0.0.0.0:8080/register";
    public static final String TAG = "RegisterAccount";

    public void registerAccount(final Context currentContext, final String mail, final String password, final RegisterActivity act, final TextView reg_info) {

        queue = Volley.newRequestQueue(currentContext);
        queue_confirm = Volley.newRequestQueue(currentContext);

        final Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user_mail", mail);
        jsonParams.put("user_password", password);


        final JsonObjectRequest postRequestForReg = new JsonObjectRequest(Request.Method.POST, account_post_url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, response.toString());

                //Wait for get chats before mail verify..
                LoginResponse loginResponse = new LoginResponse(response);

                if (loginResponse.getStatus().equals("REGISTER")) {



                } else if (loginResponse.getStatus().equals("SUCCESS")) {

                    Toast.makeText(currentContext, "Kullanici daha onceden sistemimizde kayitlidir!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(currentContext, ListViewActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("get_mail", mail); //mail parametresi diger activity'e aktarilir
                    bundle.putString("status", loginResponse.getStatus());

                    i.putExtras(bundle);

                    act.startActivity(i); //activity baslatilir

                } else if (loginResponse.getStatus().equals("MAIL_VERIFY")) {
                    Toast.makeText(currentContext, "Mail doğrulamasını yapmanız gerekmektedir! Lütfen gönderdiğimiz maili kontrol ediniz!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(currentContext, MainActivity.class);
                    act.startActivity(intent);

                } else {
                    Toast.makeText(currentContext, "Lütfen bilgilerinizi kontrol ediniz!", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Handle error
                Log.i(TAG, "REGISTER ERROR: " + error.toString());

            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_mail", mail);
                params.put("user_Password", password);
                return super.getParams(); //params;
            }
        };
        queue.add(postRequestForReg);
    }
}
