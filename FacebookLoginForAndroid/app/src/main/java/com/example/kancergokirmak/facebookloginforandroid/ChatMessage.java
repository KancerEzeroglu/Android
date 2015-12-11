package com.example.kancergokirmak.facebookloginforandroid;

/**
 * Created by kancergokirmak on 08/12/15.
 */
public class ChatMessage {

    public String message_text;

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public boolean left;

    public ChatMessage(boolean left, String message_text) {
        super();
        this.left = left;
        this.message_text = message_text;
    }

}
