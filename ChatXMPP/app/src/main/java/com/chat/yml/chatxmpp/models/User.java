package com.chat.yml.chatxmpp.models;

import java.io.Serializable;

/**
 * Created by deepak on 1/2/16.
 */
public class User implements Serializable{

    private String emailId;
    private String password;
    private boolean isMine;
    private String jid;
    private String name;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public String getJid() {
        if(jid.contains("@")) {
            return jid.split("@")[0];
        }
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
