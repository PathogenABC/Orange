package com.southstate.orange.lib;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.util.HttpUtil;
import com.southstate.orange.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
public class LibHttp {

    public static void install(PageContext pc) {
        V8 v8 = pc.getV8();
        V8Object http = new V8Object(v8);

        http.registerJavaMethod((receiver, params) -> {
            httpGet(pc, receiver, params);
        }, "get");

        v8.add("http", http);
        http.close();
    }

    /**
     * USAGE: http.get('http://abc.com/abc.json', (json, error) => {})
     */
    private static void httpGet(PageContext pc, V8Object receiver, V8Array params) {
        V8 v8 = pc.getV8();
        String url = params.getString(0);
        V8Function callback = (V8Function) params.getObject(1);
        V8Function callbackTwin = callback.twin();
        V8Object receiverTwin = receiver.twin();

        HttpUtil.httpGet(url, (result, error) -> pc.updateJS(() -> {
            V8Value resultObj;
            String errorObj;
            if (result != null && error == null) {
                try {
                    Map<String, Object> map = JsonUtil.getMapForJson(new JSONObject(result));
                    resultObj = V8ObjectUtils.toV8Object(v8, map);
                    errorObj = null;
                } catch (Exception e) {
                    try {
                        List<Object> list = JsonUtil.getListForJson(new JSONArray(result));
                        resultObj = V8ObjectUtils.toV8Array(v8, list);
                        errorObj = null;
                    } catch (Exception e1) {
                        resultObj = null;
                        errorObj = e.getMessage();
                    }
                }
            } else {
                resultObj = null;
                errorObj = error != null ? error.getMessage() : null;
            }

            V8Array args = new V8Array(v8);
            args.push(resultObj);
            args.push(errorObj);
            callbackTwin.call(receiverTwin, args);

            callbackTwin.close();
            receiverTwin.close();
            args.close();
            if (resultObj != null) {
                resultObj.close();
            }
        }));
    }

}
