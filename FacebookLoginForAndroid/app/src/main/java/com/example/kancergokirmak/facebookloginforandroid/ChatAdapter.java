package com.example.kancergokirmak.facebookloginforandroid;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kancergokirmak on 08/12/15.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    ArrayList<ChatMessage> chatMessageList = new ArrayList<>();
    LinearLayout singleMessageContainer;
    private TextView chatText;
    private ImageView profileLeft;
    private ImageView profileRight;

    public ChatAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.messaging_user_layout, parent, false);
        }
        singleMessageContainer = (LinearLayout) row.findViewById(R.id.user_linearlayout);
        ChatMessage chatMessageObj = getItem(position);
        chatText = (TextView) row.findViewById(R.id.tvBody);
        chatText.setText(chatMessageObj.message_text);
        //chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.messenger_bubble_large_blue : R.drawable.messenger_bubble_large_white);
        //singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);

        profileLeft = (ImageView) row.findViewById(R.id.ivProfileLeft);
        profileRight = (ImageView) row.findViewById(R.id.ivProfileRight);


        if (chatMessageObj.left == false){
            profileRight.setVisibility(ImageView.GONE);
            profileLeft.setVisibility(ImageView.VISIBLE);
        }else{
            profileLeft.setVisibility(ImageView.GONE);
            profileRight.setVisibility(ImageView.VISIBLE);
        }
        return row;
    }
}

