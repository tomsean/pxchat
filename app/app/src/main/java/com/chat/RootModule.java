package com.chat;

import dagger.Module;

/**
 * Created by HQ_19 on 2014/12/2.
 */
@Module(
        includes = {
                AndroidModule.class,
                ChatModule.class
        }
)
public class RootModule {
}
