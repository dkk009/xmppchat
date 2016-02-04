package com.chat.yml.chatxmpp.manager;

import com.chat.yml.chatxmpp.XMPPChatApp;
import com.chat.yml.chatxmpp.dao.TblChat;
import com.chat.yml.chatxmpp.dao.TblChatDao;
import com.chat.yml.chatxmpp.dao.TblUser;
import com.chat.yml.chatxmpp.dao.TblUserDao;

import org.jivesoftware.smack.chat.Chat;

import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by deepak on 3/2/16.
 */
public class DBManager {


    public static TblUserDao getUserInfoDaoSession() throws  Exception{
        return XMPPChatApp.getInstance().getDaoSession().getTblUserDao();
    }
    public static long insertOrUpdateUser(TblUser user) throws Exception{
        return getUserInfoDaoSession().insertOrReplace(user);
    }

    public static void clearUserInfoTable() throws Exception{
        getUserInfoDaoSession().deleteAll();
    }
    public static List<TblUser> getAllUserInfo() throws Exception {
        return  getUserInfoDaoSession().loadAll();
    }

    public static long countOfUserInfoTable() throws Exception{
        return getUserInfoDaoSession().queryBuilder().count();
    }
/*
    Get Chat Details
 */
    public static TblChatDao getChatInfoDaoSession() throws  Exception{
        return XMPPChatApp.getInstance().getDaoSession().getTblChatDao();
    }
    public static long insertOrUpdateChat(TblChat chat) throws Exception{
        return getChatInfoDaoSession().insertOrReplace(chat);
    }

    public static void clearChatInfoTable() throws Exception{
        getChatInfoDaoSession().deleteAll();
    }
    public static List<TblChat> getChatInfo(String jid) throws Exception {
        return  getChatInfoDaoSession().queryBuilder().
                where(TblChatDao.Properties.Jid.eq(jid)).orderAsc(TblChatDao.Properties.Id).list();
    }

    public static long countOfChatInfoTable(String jid) throws Exception{
        return getChatInfoDaoSession().queryBuilder().where(TblChatDao.Properties.Jid.eq(jid)).count();
    }



}
