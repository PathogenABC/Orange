package com.southstate.orange.lib;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.southstate.orange.Orange;
import com.southstate.orange.context.PageContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by junkang on 2019/5/22.
 * Function: Orange
 */
public class LibModule {

    private static final ThreadLocal<HashMap<String, V8Object>> MODULES = new ThreadLocal<>();

    /**
     * 用法：返回值可能为null，表示不支持
     * const http = modules.require('http')
     */
    public static void install(PageContext pc) {
        V8 v8 = pc.getV8();
        V8Object console = new V8Object(v8);

        MODULES.set(new HashMap<>());

        console.registerJavaMethod((v8Object, v8Array) -> {
            String moduleName = v8Array.getString(0);
            HashMap<String, V8Object> map = MODULES.get();
            if (map != null) {
                V8Object module;
                if (map.containsKey(moduleName)) {
                    module = map.get(moduleName);
                } else {
                    module = Orange.getInstance().createModule(v8, moduleName);
                    map.put(moduleName, module);
                }
                return module != null ? module.twin() : null;
            }
            return null;
        }, "require");

        v8.add("modules", console);
        console.close();
    }

    public static void release() {
        Map<String, V8Object> map = MODULES.get();
        if (map != null) {
            for (V8Object v8Object : map.values()) {
                if (!v8Object.isReleased()) {
                    v8Object.close();
                }
            }
            map.clear();
        }
    }
}
