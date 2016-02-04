package com.chat.yml.chatxmpp.manager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.chat.yml.chatxmpp.Constants.Constants;
import com.chat.yml.chatxmpp.R;
import com.chat.yml.chatxmpp.XMPPChatApp;
import com.chat.yml.chatxmpp.activities.NotificationHandlerActivity;
import com.chat.yml.chatxmpp.dao.TblChat;
import com.chat.yml.chatxmpp.interfaces.IntrNotification;
import com.chat.yml.chatxmpp.models.ChatMessage;
import com.chat.yml.chatxmpp.models.User;


import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepak on 2/2/16.
 */
public class LocalChatManager {

    public static final String TAG= LocalChatManager.class.getSimpleName();

    private static LocalChatManager instance;

    private ArrayList<User> mUserList = new ArrayList<>();
    private User mCurrentUser;
    private XMPPTCPConnection mConnection;

    public static LocalChatManager getInstance() {
        if(null == instance) {
            instance = new LocalChatManager();
        }
        return instance;
    }


    public ArrayList<User> getUserList() {
        return mUserList;
    }

    public void setUserList(ArrayList<User> mUserList) {
        this.mUserList = mUserList;
    }

    public void setCurrentUser(User user) {
        mCurrentUser = user;
    }
    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void addUser(User user) {
        if(null == mUserList) {
            mUserList = new ArrayList<>();
        }
        mUserList.add(user);
    }

    public User getUser(String jid) {
        for(User user : mUserList) {
            if(TextUtils.equals(jid,user.getJid())) {
                return user;
            }
        }
        return null;
    }



    private ChatManager mChatManager;
    public void initChat() {
        mConnection = XMPPConnectionManager.getInstance().getConnection();
        if (mConnection.isAuthenticated() && null == mChatManager) {

            mChatManager = ChatManager.getInstanceFor(mConnection);
            mChatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {

                    Log.d(TAG, "chatCreated:" + chat.getThreadID());
                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {
                            handleReceivedMessage(chat,message);
                        }
                    });
                }
            });
        }

    }

    private void handleReceivedMessage(Chat chat, Message message) {
        Log.d("message", "Message Received");
        ChatMessage receivedMessage = new ChatMessage();
        receivedMessage.setIsMine(false);
        receivedMessage.setBody(message.getBody());
        User user = LocalChatManager.getInstance().getUser(chat.getParticipant().asEntityBareJidString());
        if (null == user) {
            user = new User();
            user.setJid(chat.getParticipant().asEntityBareJidString());
        }
        receivedMessage.setSender(user);
        long id = storeReceivedMessage(receivedMessage);
        if(isNotificationRequired(receivedMessage)) {
            if (-1 != id) {
                generateNotification((int) id, receivedMessage);
            }
        }else {
            sendMessageReceivedBroadCast(receivedMessage);
        }

    }

    private long storeReceivedMessage(ChatMessage receivedMessage) {
        long id = -1;
        TblChat tblChat = new TblChat();
        tblChat.setJid(receivedMessage.getSender().getJid());
        tblChat.setMessage(receivedMessage.getBody());
        tblChat.setDate(receivedMessage.getDate());
        tblChat.setIsMine(receivedMessage.isMine());
        tblChat.setTime(receivedMessage.getTime());
        try {
            id = DBManager.insertOrUpdateChat(tblChat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


    private void sendMessageReceivedBroadCast(ChatMessage receivedMessage) {
        Intent intent = new Intent();
        intent.setAction(Constants.MESSAGE_RECIVED);
        intent.putExtra(Constants.MESSAGE_RECIVED, receivedMessage);
        LocalBroadcastManager.getInstance(XMPPChatApp.getInstance()).sendBroadcast(intent);
    }

    public ChatManager getChatManager() {
        if(null == mChatManager) {
            initChat();
        }
        return mChatManager;
    }

    public static void clear() {
        instance = null;
    }

    public List<ChatMessage> getChatMessage(User sender) {
        ArrayList<ChatMessage> chatArrayList = new ArrayList<>();
        try {
            List<TblChat> chatListFromDb = DBManager.getChatInfo(sender.getJid());
            for(TblChat tblChat : chatListFromDb) {
                ChatMessage chat = new ChatMessage();
                chat.setBody(tblChat.getMessage());
                chat.setIsMine(tblChat.getIsMine());
                chat.setTime(tblChat.getTime());
                chat.setDate(tblChat.getDate());
                if(tblChat.getIsMine()) {
                    chat.setSender(mCurrentUser);
                    chat.setReceiver(sender);
                }else {
                    chat.setSender(sender);
                    chat.setReceiver(mCurrentUser);
                }
                chatArrayList.add(chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatArrayList;
    }

    public boolean sendChatMessage(Chat chat,ChatMessage chatMessage) {
        if(null == chat) {
            return false;
        }
        try {
            chat.sendMessage(chatMessage.getBody());
            TblChat tblChat = new TblChat();

            tblChat.setTime(chatMessage.getTime());
            tblChat.setJid(chatMessage.getReceiver().getJid());
            tblChat.setIsMine(true);
            tblChat.setMessage(chatMessage.getBody());
            tblChat.setDate(chatMessage.getDate());
            DBManager.insertOrUpdateChat(tblChat);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private IntrNotification mNotificationListener;

    public void setNotificationListener(IntrNotification notificationListener) {
         mNotificationListener = notificationListener;
    }

    public boolean isNotificationRequired(ChatMessage chatMessage) {
        if(null == mNotificationListener) {
            return true;
        }else {
            User currentOpponent = mNotificationListener.getOpponent();
            User sender = chatMessage.getSender();
            if(!TextUtils.equals(sender.getJid(), currentOpponent.getJid())) {
                return true;
            }
        }
        return false;
    }
    public void generateNotification(int id,ChatMessage chatMessage) {
        Context context = XMPPChatApp.getInstance();
        String title= chatMessage.getSender().getJid();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(chatMessage.getBody());
        Intent resultIntent = new Intent(context, NotificationHandlerActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationHandlerActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        resultIntent.putExtra(Constants.SELECTED_USER,chatMessage.getSender());
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }
}
