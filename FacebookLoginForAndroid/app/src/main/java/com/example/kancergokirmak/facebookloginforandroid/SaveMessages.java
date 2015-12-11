package com.example.kancergokirmak.facebookloginforandroid;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kancergokirmak on 10/12/15.
 */
public class SaveMessages {

    RequestQueue queue_post_req_save_msg;
    String msg_post_req = "http://52.27.143.102:8080/saveMessages";
    //String msg_post_req = "http://localhost:8080/saveMessages";
    public static final String TAG = "SaveMessages";

    public void saveMessagesToDB(final Context context, final String user1, final String user2, final String content, final String date) {
        queue_post_req_save_msg = Volley.newRequestQueue(context);

        final Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("user1", user1);
        jsonParams.put("user2", user2);
        jsonParams.put("content", content);
        jsonParams.put("date", date);

        JsonObjectRequest postReqSendMsg = new JsonObjectRequest(Request.Method.POST, msg_post_req, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Basarili bir sekilde SaveMessages POST Request yapti!!");
                Toast.makeText(context, "Mesajiniz kaydedilmistir", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "LOGIN ERROR for save messages: " + error.toString());
            }
        }
        ) {
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", user1);
                params.put("content", content);
                params.put("date", date);

                return super.getParams(); //params;
            }*/
        };
        queue_post_req_save_msg.add(postReqSendMsg);

        postReqSendMsg.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
