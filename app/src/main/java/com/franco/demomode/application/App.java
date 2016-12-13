package com.franco.demomode.application;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    public static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }
}
