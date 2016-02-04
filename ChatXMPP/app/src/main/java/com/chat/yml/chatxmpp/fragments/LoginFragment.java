package com.chat.yml.chatxmpp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.yml.chatxmpp.R;
import com.chat.yml.chatxmpp.Utilities.CommonUtilities;
import com.chat.yml.chatxmpp.activities.ChatActivity;
import com.chat.yml.chatxmpp.models.User;
import com.chat.yml.chatxmpp.network.ConnectionTask;

/**
 * Created by deepak on 1/2/16.
 */
public class LoginFragment extends Fragment {


    private TextView mTxtFieldEmail;
    private EditText mTxtFieldPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTxtFieldEmail = (EditText) view.findViewById(R.id.txt_field_email);
        mTxtFieldPassword = (EditText) view.findViewById(R.id.txt_field_password);
        TextView txtConnect = (TextView) view.findViewById(R.id.txt_connect);
        txtConnect.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txt_connect:
                    handleConnectClick();
                    break;
            }
        }
    };

    private void handleConnectClick() {
        User user = new User();
        String email = mTxtFieldEmail.getText().toString();
        String password = mTxtFieldPassword.getText().toString();
        user.setEmailId(email);
        user.setUserName(email);
        user.setPassword(password);
        if(!isValidLoginDetails(user)) {
            Toast.makeText(getActivity(),"Invalid Login Details",Toast.LENGTH_SHORT).show();
            return;
        }
        ConnectionTask connectionTask = new ConnectionTask(new ConnectionTask.IntrConnectionListener() {
            @Override
            public void completed(boolean status) {
                CommonUtilities.hideProgressDialog();
                if(status) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(),"Invalid Login Details",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        },user);
        connectionTask.execute();
        CommonUtilities.showProgressDialog(getActivity());
    }

    private boolean isValidLoginDetails(User user) {
        if(TextUtils.isEmpty(user.getEmailId()) || TextUtils.isEmpty(user.getPassword())) {
            return false;
        }
        return true;
    }


}
