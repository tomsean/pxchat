package com.chat.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import static android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT;

/**
 * Created by HQ_19 on 2014/12/19.
 */
public class AccountAuthenticatorService extends Service {
    private static BootstrapAccountAuthenticator authenticator = null;
    @Override
    public  void onCreate() {
        super.onCreate();
       //
    }
    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null && ACTION_AUTHENTICATOR_INTENT.equals(intent.getAction())) {
            return getAuthenticator().getIBinder();
        }
        return null;
    }
    private BootstrapAccountAuthenticator getAuthenticator() {

        Log.i("auth_service","create");
        if (authenticator == null) {
            authenticator = new BootstrapAccountAuthenticator(this);
        }
        return authenticator;
    }
}
