package com.chat.yml.chatxmpp.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.yml.chatxmpp.Constants.Constants;
import com.chat.yml.chatxmpp.R;
import com.chat.yml.chatxmpp.fragments.ChatViewFragment;
import com.chat.yml.chatxmpp.interfaces.IntrFragmentCommunicator;
import com.chat.yml.chatxmpp.manager.LocalChatManager;
import com.chat.yml.chatxmpp.models.User;

/**
 * Created by deepak on 3/2/16.
 */
public class NotificationHandlerActivity extends AppCompatActivity implements IntrFragmentCommunicator {

    private TextView mTxtUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTxtUserInfo = (TextView) toolbar.findViewById(R.id.txt_user_info);
        LocalChatManager.getInstance().initChat();
        User selectedUser = (User) getIntent().getSerializableExtra(Constants.SELECTED_USER);
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
        if(null != selectedUser) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.SELECTED_USER, selectedUser);
            ChatViewFragment chatViewFragment = new ChatViewFragment();
            chatViewFragment.setArguments(bundle);
            loadFragment(chatViewFragment, ChatViewFragment.class.getSimpleName(), false);
        }else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        launchActivity();
    }

    private void launchActivity() {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
        mTxtUserInfo.setText(title);
    }

    @Override
    public void popFragment() {

    }
}
