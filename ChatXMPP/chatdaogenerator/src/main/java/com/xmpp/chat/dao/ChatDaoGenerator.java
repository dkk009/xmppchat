package com.xmpp.chat.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ChatDaoGenerator {

    public static void main(String args[]) {
        Schema schema = new Schema(1, "com.chat.yml.chatxmpp.dao");
        Entity userTbl = schema.addEntity("TblUser");
        userTbl.addIdProperty().autoincrement().primaryKey();
        userTbl.addStringProperty("userName");
        userTbl.addStringProperty("emailId");
        userTbl.addStringProperty("jid").notNull().unique();
        userTbl.addStringProperty("name");
        userTbl.addIntProperty("type");

        Entity chatTbl = schema.addEntity("TblChat");
        chatTbl.addIdProperty().autoincrement().primaryKey();
        chatTbl.addStringProperty("jid");
        chatTbl.addStringProperty("message");
        chatTbl.addStringProperty("date");
        chatTbl.addStringProperty("time");
        chatTbl.addBooleanProperty("isMine");
        try {
            new DaoGenerator().generateAll(schema,"app/src/main/java" );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Dao Exception:" + e);
        }
    }
}
