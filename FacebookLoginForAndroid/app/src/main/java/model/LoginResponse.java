package model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kancergokirmak on 01/12/15.
 */
public class LoginResponse {

    String message;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResponse(JSONObject jsonObject) {

        try {
            this.message = jsonObject.getString("message");
            this.status = jsonObject.getString("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
