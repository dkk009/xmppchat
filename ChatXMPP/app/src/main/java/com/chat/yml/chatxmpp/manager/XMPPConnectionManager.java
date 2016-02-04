package com.chat.yml.chatxmpp.manager;

import android.text.TextUtils;
import android.util.Log;

import com.chat.yml.chatxmpp.Constants.Constants;
import com.chat.yml.chatxmpp.models.User;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by deepak on 29/1/16.
 */
public class XMPPConnectionManager {

    public static final String TAG = XMPPConnectionManager.class.getSimpleName();

    public static final String HOST = "172.16.1.39";//"talk.google.com"; //"chat.facebook.com";
    public static final int PORT = 5222;//5222;
    public static final String SERVICE = "jabber.org";//"gmail.com";

    private static XMPPConnectionManager instance = null;

    public static synchronized XMPPConnectionManager getInstance() {
        if (null == instance) {
            instance = new XMPPConnectionManager();
        }
        return instance;
    }

    private XMPPTCPConnection mConnection;
    private User mUser;

    public void connect(User user) throws Exception {
        mUser = user;

        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();

        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
        builder.setHost(HOST);
        DomainBareJid serviceName = JidCreate.domainBareFrom(SERVICE);
        builder.setXmppDomain(serviceName);
        builder.setPort(PORT);

        builder.setResource("SmackAndroidTestClient");
        builder.setUsernameAndPassword(mUser.getEmailId(), mUser.getPassword());
        builder.setDebuggerEnabled(true);

        try {


            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
// Initialise the TMF as you normally would, for example:
            tmf.init((KeyStore) null);

            TrustManager[] trustManagers = tmf.getTrustManagers();
            final X509TrustManager origTrustmanager = (X509TrustManager) trustManagers[0];

            TrustManager[] wrappedTrustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return origTrustmanager.getAcceptedIssuers();
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustmanager.checkClientTrusted(certs, authType);
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustmanager.checkServerTrusted(certs, authType);
                        }
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, wrappedTrustManagers, null);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (KeyManagementException e) {
            throw new IllegalStateException(e);
        }
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        mConnection = new XMPPTCPConnection(builder.build());
        mConnection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {
                Log.d(TAG, "connected:" + connection.getStreamId());
            }

            @Override
            public void authenticated(XMPPConnection connection, boolean resumed) {
                Log.d(TAG, "authenticated,Resumed:" + resumed);
            }

            @Override
            public void connectionClosed() {
                Log.d(TAG, "connectionClosed");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                Log.d(TAG, "connectionClosedOnError:" + e);
            }

            @Override
            public void reconnectionSuccessful() {
                Log.d(TAG, "reconnectionSuccessful");
            }

            @Override
            public void reconnectingIn(int seconds) {
                Log.d(TAG, "reconnectingIn:" + seconds);
            }

            @Override
            public void reconnectionFailed(Exception e) {
                Log.d(TAG, "reconnectionFailed:" + e);
            }
        });

        mConnection.setPacketReplyTimeout(10000);
        mConnection.connect();
        mConnection.login();
    }

    public XMPPTCPConnection getConnection() {
        return mConnection;
    }


    public User getUser() {
        return mUser;
    }

    public void getAllUsers() throws XmppStringprepException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, InterruptedException, SmackException.NoResponseException {
        UserSearchManager userSearchManager = new UserSearchManager(mConnection);
        Form searchForm = userSearchManager.getSearchForm(JidCreate.domainBareFrom("search." + mConnection.getServiceName()));
        Form answerForm = searchForm.createAnswerForm();
        UserSearch userSearch = new UserSearch();
        answerForm.setAnswer("Username", true);

        answerForm.setAnswer("search", "*");
        ReportedData data = userSearch.sendSearchForm(mConnection, answerForm, JidCreate.domainBareFrom("search." + mConnection.getServiceName()));
        if (null != data.getRows()) {
            User user = null;
            for (ReportedData.Row row : data.getRows()) {
                user = new User();
                String userName = row.getValues(Constants.USER_NAME).get(0);

                user.setEmailId(row.getValues(Constants.EMAIL).get(0));
                user.setName(row.getValues(Constants.NAME).get(0));
                user.setJid(row.getValues(Constants.JID).get(0));
                if(TextUtils.equals(userName, XMPPConnectionManager.getInstance().getUser().getUserName())) {
                    user.setUserName(row.getValues(Constants.USER_NAME).get(0));
                    user.setIsMine(true);
                    LocalChatManager.getInstance().setCurrentUser(user);
                    continue;
                }
                LocalChatManager.getInstance().addUser(user);
            }
        }
    }

    public static void clear() {
        instance = null;
    }
}
