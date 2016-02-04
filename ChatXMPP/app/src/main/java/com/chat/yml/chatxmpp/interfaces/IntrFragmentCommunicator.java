package com.chat.yml.chatxmpp.interfaces;

import android.support.v4.app.Fragment;

/**
 * Created by deepak on 29/1/16.
 */
public interface IntrFragmentCommunicator {
    void loadFragment(Fragment fragment,String tag,boolean isAddedToBackStack);
    void updateTitle(String title);
    void popFragment();
}
