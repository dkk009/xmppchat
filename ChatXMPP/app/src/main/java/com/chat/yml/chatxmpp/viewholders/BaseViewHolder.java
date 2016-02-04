package com.chat.yml.chatxmpp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Objects;

/**
 * Created by deepak on 29/1/16.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(Objects object);
}
