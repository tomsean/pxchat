package com.chat;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

/**
 * Created by HQ_19 on 2014/12/2.
 */
public class ChatApplication extends Application {
    private static ChatApplication instance;

    /**
     * Create main application
     */
    public ChatApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public ChatApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Perform injection
        Injector.init(getRootModule(), this);

    }

    private Object getRootModule() {
        return new RootModule();
    }


    /**
     * Create main application
     *
     * @param instrumentation
     */
    public ChatApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static ChatApplication getInstance() {
        return instance;
    }
}
