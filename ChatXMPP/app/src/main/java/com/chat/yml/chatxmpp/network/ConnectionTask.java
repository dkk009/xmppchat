package com.chat.yml.chatxmpp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.chat.yml.chatxmpp.manager.XMPPConnectionManager;
import com.chat.yml.chatxmpp.models.User;

/**
 * Created by deepak on 29/1/16.
 */
public class ConnectionTask extends AsyncTask<Void,Void,Boolean> {

    public static final String TAG = ConnectionTask.class.getSimpleName();
    private IntrConnectionListener mIntrConnectionListener;
    private User mUser;

    public ConnectionTask(IntrConnectionListener intrConnectionListener,User user) {
        mIntrConnectionListener = intrConnectionListener;
        mUser = user;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        boolean status = true;
        try {
            XMPPConnectionManager.getInstance().connect(mUser);
            Log.d(TAG, "Connection Success:");
            XMPPConnectionManager.getInstance().getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Connection Error:" + e);
            status = false;

        }
        return status;
    }

    @Override
    protected void onPostExecute(Boolean status) {
        super.onPostExecute(status);
        if(null != mIntrConnectionListener) {
            mIntrConnectionListener.completed(status);
        }
        Log.d(TAG, "Connection Status:" + status);
    }

    public interface IntrConnectionListener {
        public void completed(boolean status) ;
    }
}
