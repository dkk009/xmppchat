package com.chat.yml.chatxmpp.viewholders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chat.yml.chatxmpp.R;
import com.chat.yml.chatxmpp.models.ChatMessage;

import java.util.Objects;

/**
 * Created by deepak on 29/1/16.
 */
public class ChatViewHolder extends RecyclerView.ViewHolder {
    private TextView mTxtChatMessage;
    private Context mContext;
    public ChatViewHolder(View itemView) {
        super(itemView);
        mTxtChatMessage = (TextView) itemView.findViewById(R.id.txt_message);
        mContext = itemView.getContext();
    }

    public void bindData(ChatMessage chatMessage) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTxtChatMessage.getLayoutParams();
        int verb = 0;
        if(chatMessage.isMine()) {
            mTxtChatMessage.setBackgroundResource(R.drawable.bubble1);
            layoutParams.gravity = Gravity.RIGHT;

        }else {
            mTxtChatMessage.setBackgroundResource(R.drawable.bubble2);
            layoutParams.gravity = Gravity.LEFT;
        }
        mTxtChatMessage.setText(chatMessage.getBody());


    }
}
