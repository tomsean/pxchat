package com.chat;

import android.accounts.AccountsException;
import android.app.Activity;

import java.io.IOException;

import retrofit.RestAdapter;

/**
 * Created by HQ_19 on 2014/12/2.
 */
public class ChatServiceProvider {
    private RestAdapter restAdapter;

    public ChatServiceProvider(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public ChatServiceProvider getService(final Activity activity)
            throws IOException, AccountsException {
        // The call to keyProvider.getAuthKey(...) is what initiates the login screen. Call that now.
        // keyProvider.getAuthKey(activity);

        // TODO: See how that affects the bootstrap service.
        return new ChatServiceProvider(restAdapter);
    }
}
