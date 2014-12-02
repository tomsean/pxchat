package com.chat.core;

import java.util.List;

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

}
