package com.southstate.orange.util;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
public class DomUtil {

    private static final String KEY_REF = "__ref_";

    public static V8Object getJSNodeByRef(V8 v8, String ref) {
        V8Object v8Object = new V8Object(v8);
        v8Object.add(KEY_REF, ref);
        return v8Object;
    }

}
