package com.chat.yml.chatxmpp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.yml.chatxmpp.Constants.Constants;
import com.chat.yml.chatxmpp.Utilities.ChatUtilities;
import com.chat.yml.chatxmpp.interfaces.IntrFragmentCommunicator;
import com.chat.yml.chatxmpp.interfaces.IntrNotification;
import com.chat.yml.chatxmpp.manager.LocalChatManager;
import com.chat.yml.chatxmpp.R;
import com.chat.yml.chatxmpp.adapters.ChatAdapter;
import com.chat.yml.chatxmpp.models.ChatMessage;
import com.chat.yml.chatxmpp.models.User;

import org.jivesoftware.smack.chat.Chat;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepak on 29/1/16.
 */
public class ChatViewFragment extends Fragment implements IntrNotification{

    private static final String TAG = ChatViewFragment.class.getSimpleName();
    private RecyclerView mChatListView;
    private EditText mTxtFieldChat;
    private ChatAdapter mChatListAdapter;
    private List<ChatMessage> mChatMessageList;
    private User mReceiver;
    private Chat mChat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_view,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mChatListView = (RecyclerView)view.findViewById(R.id.chat_view_list);
        mTxtFieldChat = (EditText)view.findViewById(R.id.edit_text_message);
        View btnSend = view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(mOnClickListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }



    private void init() {
        mReceiver = (User) getArguments().get(Constants.SELECTED_USER);
        createChat(mReceiver);
        LocalChatManager.getInstance().setNotificationListener(this);
        mChatMessageList = LocalChatManager.getInstance().getChatMessage(mReceiver);
        mChatListAdapter = new ChatAdapter(mChatMessageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mChatListView.setLayoutManager(layoutManager);
        mChatListView.setAdapter(mChatListAdapter);
        if(mChatMessageList != null && !mChatMessageList.isEmpty()) {
            mChatListView.scrollToPosition(mChatMessageList.size()-1);
        }


        if(getActivity() instanceof IntrFragmentCommunicator) {
            ((IntrFragmentCommunicator)getActivity()).updateTitle(mReceiver.getJid());
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.MESSAGE_RECIVED));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ChatMessage chatMessage = (ChatMessage) intent.getSerializableExtra(Constants.MESSAGE_RECIVED);
            if(TextUtils.equals(chatMessage.getSender().getJid(),mReceiver.getJid()) ) {
                if(null == mChatMessageList) {
                    mChatMessageList = new ArrayList<>();
                }
                mChatMessageList.add(chatMessage);
                mChatListAdapter.notifyItemChanged(mChatMessageList.size());
                mChatListView.scrollToPosition(mChatMessageList.size()-1);
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        LocalChatManager.getInstance().setNotificationListener(null);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send:
                    handleSendBtnClick();
                    break;
            }
        }
    };

    private void handleSendBtnClick() {
        String message = mTxtFieldChat.getText().toString();
        if(!TextUtils.isEmpty(message)) {
            sendMessage(message);
        }
    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = ChatUtilities.prepareChatMessage(message,mReceiver);
            if (null == mChat) {
                createChat(chatMessage.getReceiver());
            }
            boolean status = LocalChatManager.getInstance().sendChatMessage(mChat,chatMessage);
        if(status) {
            mChatMessageList.add(mChatMessageList.size(), chatMessage);
            mChatListAdapter.notifyItemChanged(mChatMessageList.size());
            mChatListView.scrollToPosition(mChatMessageList.size() - 1);
            mTxtFieldChat.setText("");
        }else {
            Toast.makeText(getActivity(),"Failed to send",Toast.LENGTH_SHORT).show();
        }
    }


    public void createChat(User user) {
        Jid receiver = null;
        try {
            receiver = (EntityJid) JidCreate.from(user.getJid(),
                    Constants.DOMAIN_NAME, Constants.RESOURCE_NAME);

        } catch (XmppStringprepException e) {
            e.printStackTrace();
            Log.d(TAG, "Entity Exception" + e);
        }
        mChat = LocalChatManager.getInstance().getChatManager().createChat((EntityJid) receiver);
    }


    @Override
    public User getOpponent() {
        return mReceiver;
    }
}
