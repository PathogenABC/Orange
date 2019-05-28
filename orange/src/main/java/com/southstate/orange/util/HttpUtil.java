package com.southstate.orange.util;

import java.io.IOException;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
public class HttpUtil {

    public static void httpGet(String url, Callback<String> callback) {
        new Thread(() -> {
            try {
                String json = FileUtil.httpGetString(url);
                callback.result(json, null);
            } catch (IOException e) {
                callback.result(null, e);
            }
        }).start();
    }

    public interface Callback<T> {
        void result(T result, Throwable error);
    }
}
