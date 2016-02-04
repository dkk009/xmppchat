package com.chat.yml.chatxmpp.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.chat.yml.chatxmpp.models.User;

/**
 * Created by deepak on 29/1/16.
 */
public class CommonUtilities {

    private static ProgressDialog mProgressDialog;
    public static void hideKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showProgressDialog(Activity activity) {
        if(null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(activity);
        }
        if(!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public static void hideProgressDialog() {
        if(null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static User getTestUser() {

        /*final String userName = "ytest27@gmail.com";
        final String password = "admin123*";*/

       final String userName = "ytest27";
        final String password = "admin123";

        /*final String userName = "testingacc";
        final String password = "admin123";*/
        User user = new User();
        user.setEmailId(userName);
        user.setPassword(password);
        return user;
    }
    public static User getTestReceiver() {

        /*final String userName = "ytest27";
        final String password = "admin123";*/

       final String userName = "testingacc";
        final String password = "admin123";

        /*final String userName = "testingacc251@gmail.com";
        final String password = "qwerty1234@#";*/
        User user = new User();
        user.setEmailId(userName);
        user.setPassword(password);
        return user;
    }



}
