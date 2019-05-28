package com.southstate.orange.context;

import android.content.Context;

import com.eclipsesource.v8.V8;
import com.southstate.orange.JSThread;
import com.southstate.orange.OrangeView;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
public class PageContext {

    private JSThread mThread;

    public PageContext(JSThread jsThread) {
        mThread = jsThread;
    }

    public JSThread getThread() {
        return mThread;
    }

    public V8 getV8() {
        return mThread.getV8();
    }

    public Context getContext() {
        return mThread.getContext();
    }

    public OrangeView getView() {
        return mThread.getView();
    }

    public void updateUI(Runnable runnable) {
        mThread.updateUI(runnable);
    }

    public void updateJS(Runnable runnable) {
        mThread.updateJS(runnable);
    }
}
