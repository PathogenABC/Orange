package com.southstate.orange.util;

import android.content.Context;

/**
 * Created by junkang on 2019/5/18.
 * Function: J2V8Demo
 */
public abstract class ErrSafeRunnable implements Runnable {

    private final Context context;

    public ErrSafeRunnable(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            ErrorUtil.handleJsError(context, e);
        }
    }

    protected abstract void call();
}
