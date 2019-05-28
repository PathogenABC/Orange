package com.southstate.orange.lib;

import android.util.Log;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.southstate.orange.context.PageContext;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
public class LibConsole {

    public static void install(PageContext pc) {
        V8 v8 = pc.getV8();
        V8Object console = new V8Object(v8);

        console.registerJavaMethod((v8Object, v8Array) -> {
            String tag = v8Array.getString(0);
            String msg = String.valueOf(v8Array.get(1));
            Log.d(tag, msg);
        }, "log");

        v8.add("console", console);
        console.close();
    }
}
