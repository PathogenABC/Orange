package com.southstate.test.orange;

import android.app.Application;

import com.southstate.orange.Orange;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Orange.init(new Orange.Builder(this).registerModule(UserModule.class));
    }
}
