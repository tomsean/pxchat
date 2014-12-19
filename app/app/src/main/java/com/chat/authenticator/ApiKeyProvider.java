package com.chat.authenticator;

import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AccountsException;
import android.app.Activity;
import android.os.Bundle;

import com.chat.core.Constants;

import java.io.IOException;

import static android.accounts.AccountManager.KEY_AUTHTOKEN;

/**
 * Bridge class that obtains a API key for the currently configured account
 */
public class ApiKeyProvider {

    private AccountManager accountManager;

    public ApiKeyProvider(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * This call blocks, so shouldn't be called on the UI thread.
     * This call is what makes the login screen pop up. If the user has
     * not logged in there will no accounts in the {@link android.accounts.AccountManager}
     * and therefore the Activity that is referenced in the
     * {@link com} will get started.
     * If you want to remove the authentication then you can comment out the code below and return a string such as
     * "foo" and the authentication process will not be kicked off. Alternatively, you can remove this class
     * completely and clean up any references to the authenticator.
     *
     *
     * @return API key to be used for authorization with a
     * {@link com.} instance
     * @throws android.accounts.AccountsException
     * @throws java.io.IOException
     */
    public String getAuthKey(final Activity activity) {
        try {

            final AccountManagerFuture<Bundle> accountManagerFuture
                    = accountManager.getAuthTokenByFeatures(Constants.Auth.BOOTSTRAP_ACCOUNT_TYPE,
                    Constants.Auth.AUTHTOKEN_TYPE, new String[0], activity, null, null, null, null);
            Bundle bundle=  accountManagerFuture.getResult();
            String result=bundle.getString(KEY_AUTHTOKEN);
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }
}

