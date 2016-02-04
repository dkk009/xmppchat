package com.chat.yml.chatxmpp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.chat.yml.chatxmpp.R;
import com.chat.yml.chatxmpp.fragments.LoginFragment;
import com.chat.yml.chatxmpp.interfaces.IntrFragmentCommunicator;
import com.chat.yml.chatxmpp.manager.LocalChatManager;
import com.chat.yml.chatxmpp.manager.XMPPConnectionManager;

/**
 * Created by deepak on 2/2/16.
 */
public class AuthenticationActivity extends AppCompatActivity implements IntrFragmentCommunicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.txt_user_info);
        txtTitle.setText(getString(R.string.xmpp_chat));
        LocalChatManager.clear();
        XMPPConnectionManager.clear();
        loadFragment(new LoginFragment(), LoginFragment.class.getSimpleName(), false);

    }

    @Override
    public void loadFragment(Fragment fragment, String tag, boolean isAddedToBackStack) {
        if (isAddedToBackStack) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.main_activity_container, fragment, tag).
                    addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.main_activity_container, fragment, tag)
                    .commit();
        }
    }

    @Override
    public void updateTitle(String title) {

    }

    @Override
    public void popFragment() {

    }
}

