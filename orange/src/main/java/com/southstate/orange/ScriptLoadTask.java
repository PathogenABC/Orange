package com.southstate.orange;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.southstate.orange.util.FileUtil;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
class ScriptLoadTask {

    private final String url;
    private final Callback callback;
    private AtomicBoolean cancelled = new AtomicBoolean(false);
    private Thread thread;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public ScriptLoadTask(String url, @NonNull Callback callback) {
        this.url = url;
        this.callback = callback;
    }

    public void cancel() {
        cancelled.set(true);
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
    }

    public void load() {
        thread = new Thread(() -> {
            if (cancelled.get()) {
                return;
            }
            try {
                String js = FileUtil.getString(url);
                uiHandler.post(() -> {
                    if (cancelled.get()) {
                        return;
                    }
                    callback.onLoadFinished(js, null);
                });
            } catch (IOException e) {
                e.printStackTrace();
                uiHandler.post(() -> {
                    if (cancelled.get()) {
                        return;
                    }
                    callback.onLoadFinished(null, e);
                });
            }
        });
        thread.start();
    }

    public interface Callback {
        void onLoadFinished(String js, Throwable error);
    }

}
