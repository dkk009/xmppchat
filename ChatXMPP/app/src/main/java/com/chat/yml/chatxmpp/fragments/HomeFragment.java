package com.chat.yml.chatxmpp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chat.yml.chatxmpp.Constants.Constants;
import com.chat.yml.chatxmpp.manager.LocalChatManager;
import com.chat.yml.chatxmpp.activities.ChatActivity;
import com.chat.yml.chatxmpp.R;
import com.chat.yml.chatxmpp.interfaces.IntrFragmentCommunicator;
import com.chat.yml.chatxmpp.models.User;

import java.util.ArrayList;

/**
 * Created by deepak on 2/2/16.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerUserList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerUserList = (RecyclerView) view.findViewById(R.id.recycler_user_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        ChatUserAdapter chatUserAdapter = new ChatUserAdapter(LocalChatManager.getInstance().getUserList());
        mRecyclerUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerUserList.setAdapter(chatUserAdapter);
        if(getActivity() instanceof IntrFragmentCommunicator) {
            ((IntrFragmentCommunicator)getActivity()).updateTitle(LocalChatManager.getInstance().getCurrentUser().getName());
        }
    }

    private  class ChatUserViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtName;
        private TextView mTxtEmail;
        private View mView;
        public ChatUserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTxtEmail = (TextView) itemView.findViewById(R.id.txt_email);
            mTxtName = (TextView) itemView.findViewById(R.id.txt_name);
        }

        public void bindData(User userInfo) {
            mTxtName.setText(userInfo.getName());
            mTxtEmail.setText(userInfo.getEmailId());
            mView.setTag(userInfo);
            if(userInfo.isMine()) {
                mView.setOnClickListener(null);
            }else {
                mView.setOnClickListener(mOnClickListener);
            }
        }

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof IntrFragmentCommunicator) {
                    ChatViewFragment chatViewFragment = new ChatViewFragment();
                    Bundle bundle=  new Bundle();
                    bundle.putSerializable(Constants.SELECTED_USER, (User) v.getTag());
                    chatViewFragment.setArguments(bundle);
                    ((IntrFragmentCommunicator)getActivity()).
                            loadFragment(chatViewFragment, ChatViewFragment.class.getSimpleName(), true);
                }
            }
        };
    }



    private class ChatUserAdapter extends RecyclerView.Adapter<ChatUserViewHolder> {

        private ArrayList<User> mUserList;
        public ChatUserAdapter(ArrayList<User> userList) {
            this.mUserList = userList;
        }
        @Override
        public ChatUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_info,parent,false);
            ChatUserViewHolder chatUserViewHolder = new ChatUserViewHolder(view);
            return chatUserViewHolder;
        }

        @Override
        public void onBindViewHolder(ChatUserViewHolder holder, int position) {
            holder.bindData(mUserList.get(position));
        }

        @Override
        public int getItemCount() {
            return mUserList == null?0:mUserList.size();
        }
    }
}
