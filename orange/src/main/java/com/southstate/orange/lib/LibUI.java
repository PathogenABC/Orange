package com.southstate.orange.lib;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.southstate.orange.OrangeView;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.NodeFactory;

import static com.southstate.orange.util.DimenUtil.dot;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
public class LibUI {

    public static void install(PageContext pc) {
        V8 v8 = pc.getV8();
        OrangeView view = pc.getView();
        V8Object ui = new V8Object(v8);

        ui.registerJavaMethod((v8Object, v8Array) -> {
            String tag = v8Array.getString(0);
            return NodeFactory.createNode(pc, tag);
        }, "createElement");

        pc.updateUI(() -> {
            // in ui
            view.addOnSizeChangeListener((width, height) -> {
                // in js
                pc.updateJS(() -> {
                    float widthDot = dot(view.getWidth());
                    float heightDot = dot(view.getHeight());
                    V8Object ui1 = v8.getObject("ui");
                    ui1.add("width", widthDot);
                    ui1.add("height", heightDot);
                    ui1.close();
                });
            });
        });
        float widthDot = dot(view.getWidth());
        float heightDot = dot(view.getHeight());
        ui.add("width", widthDot);
        ui.add("height", heightDot);

        v8.add("ui", ui);
        ui.close();
    }
}
