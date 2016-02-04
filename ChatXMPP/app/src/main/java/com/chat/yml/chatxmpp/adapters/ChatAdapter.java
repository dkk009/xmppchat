package com.chat.yml.chatxmpp.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.yml.chatxmpp.R;
import com.chat.yml.chatxmpp.models.ChatMessage;
import com.chat.yml.chatxmpp.viewholders.ChatViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepak on 29/1/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<ChatMessage> mMessageList;
    public  ChatAdapter(List<ChatMessage> list) {
        mMessageList = list;
    }
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat,parent,false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.bindData(mMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return null == mMessageList ? 0 : mMessageList.size();
    }
}
