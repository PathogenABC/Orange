package com.southstate.orange.lib;

import android.content.Context;
import android.widget.Toast;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.southstate.orange.context.PageContext;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
public class LibApp {

    public static void install(PageContext pc) {
        V8 v8 = pc.getV8();
        Context context = pc.getContext();
        V8Object app = new V8Object(v8);

        app.registerJavaMethod((v8Object, v8Array) -> {
            String msg = v8Array.getString(0);
            pc.updateUI(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
        }, "toast");

        v8.add("app", app);
        app.close();
    }

}
