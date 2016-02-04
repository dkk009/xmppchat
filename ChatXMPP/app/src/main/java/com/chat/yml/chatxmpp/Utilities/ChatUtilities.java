package com.chat.yml.chatxmpp.Utilities;

import com.chat.yml.chatxmpp.manager.LocalChatManager;
import com.chat.yml.chatxmpp.models.ChatMessage;
import com.chat.yml.chatxmpp.models.User;

/**
 * Created by deepak on 3/2/16.
 */
public class ChatUtilities {

    public static ChatMessage prepareChatMessage(String message,User receiver) {
        ChatMessage chatMessage  = new ChatMessage();
        chatMessage.setIsMine(true);
        chatMessage.setBody(message);
        chatMessage.setDate(DateUtilities.getCurrentDate());
        chatMessage.setTime(DateUtilities.getCurrentTime());
        chatMessage.setSender(LocalChatManager.getInstance().getCurrentUser());
        chatMessage.setReceiver(receiver);
        return chatMessage;
    }
}
