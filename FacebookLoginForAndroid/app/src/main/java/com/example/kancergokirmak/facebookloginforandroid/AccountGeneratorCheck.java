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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import model.LoginResponse;

/**
 * Created by kancergokirmak on 26/11/15.
 */
public class AccountGeneratorCheck {

    RequestQueue queue;
    String account_post_url = "http://52.27.143.102:8080/login";
    public static final String TAG = "AccountGeneratorCheck";

    public void checkMemberAccount(final Context currentContext, final String mail, final String password, final MainActivity act, final TextView info) {

        queue = Volley.newRequestQueue(currentContext);

        final Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user_mail", mail);
        jsonParams.put("user_password", password);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, account_post_url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, response.toString());

                LoginResponse loginResponse = new LoginResponse(response);

                if (loginResponse.getStatus().equals("SUCCESS")){
                    act.send_message_start(); // 2.layout baslatilir
                }else{
                    Toast.makeText(currentContext, "Lütfen bilgilerinizi kontrol ediniz!",Toast.LENGTH_SHORT).show();
                    info.setText("Kullanici kayitli değildir!");
                }

                /*try{
                    //JSONObject value = response.getJSONObject();
                    //JSONArray jr = value.getJSONArray("member");

                    for (int i =0; i < jr.length();i++){
                        JSONObject jb = (JSONObject) jr.get(i);
                        if (i==0){
                            String userMail = jb.getString("user_mail");
                            Log.i(TAG,"Mail alindi: "+ userMail);
                        }else if (i==1){
                            String userPassword = jb.getString("user_password");
                            Log.i(TAG,"Parola alindi: "+userPassword);
                        }

                    }
                }catch (Exception e){
                    Log.e(TAG,"Hata oldu!");
                    e.printStackTrace();
                }*/

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Handle error
                        Log.i(TAG, "LOGIN ERROR: " + error.toString());

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
        queue.add(postRequest);
    }
}
