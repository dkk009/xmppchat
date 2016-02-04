package com.chat.yml.chatxmpp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.chat.yml.chatxmpp.dao.DaoMaster;
import com.chat.yml.chatxmpp.dao.DaoSession;

/**
 * Created by deepak on 29/1/16.
 */
public class XMPPChatApp extends Application {

    private static XMPPChatApp instance;
    private static final String DB_NAME = "xmppChat";

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDb();
    }

    public static XMPPChatApp getInstance() {
        return instance;
    }

    public void initDb() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,DB_NAME,null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
