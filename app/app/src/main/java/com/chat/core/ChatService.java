package com.chat.core;

import com.chat.ui.model.User;

import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class ChatService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public ChatService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public ChatService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public List<User> getUsers() {
        return getUserService().getUsers().getResults();
    }

    public Map<String, User> getContactList() {
        return null;
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }
}
