package com.chat;

import com.chat.core.PostFromAnyThreadBus;
import com.chat.core.RestErrorHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by HQ_19 on 2014/12/2.
 */
@Module(
        complete = false,
        injects = {
                ChatApplication.class
        }
)
public class ChatModule {
    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }
}
