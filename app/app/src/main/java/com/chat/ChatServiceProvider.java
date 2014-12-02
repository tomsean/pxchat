package com.chat;

import android.accounts.AccountsException;
import android.app.Activity;

import com.chat.authenticator.ApiKeyProvider;
import com.chat.core.ChatService;

import java.io.IOException;

import retrofit.RestAdapter;

/**
 * Created by HQ_19 on 2014/12/2.
 */
public class ChatServiceProvider {
    private RestAdapter restAdapter;
    private ApiKeyProvider keyProvider;

    public ChatServiceProvider(RestAdapter restAdapter,ApiKeyProvider apiKeyProvider) {
        this.restAdapter = restAdapter;
        this.keyProvider=apiKeyProvider;
    }

    public ChatService getService(final Activity activity)
            throws IOException, AccountsException {
        // The call to keyProvider.getAuthKey(...) is what initiates the login screen. Call that now.
         keyProvider.getAuthKey(activity);

        // TODO: See how that affects the bootstrap service.
        return new ChatService(restAdapter);
    }
}
