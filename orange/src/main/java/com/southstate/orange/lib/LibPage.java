package com.southstate.orange.lib;

import android.content.Context;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.southstate.orange.Orange;
import com.southstate.orange.OrangeView;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.util.PageUri;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
public class LibPage {

    private final Map<String, Set<V8Function>> eventHandlerMap = new HashMap<>();

    private final PageContext mPageContext;

    public LibPage(PageContext pc) {
        this.mPageContext = pc;
    }

    public void install(PageUri pageUri, V8Object jsRootNode, V8Object param) {
        V8 v8 = mPageContext.getV8();
        OrangeView view = mPageContext.getView();
        Context context = mPageContext.getContext();

        V8Object page = new V8Object(v8);

        page.registerJavaMethod((v8Object, v8Array) -> {
            String event = v8Array.getString(0);
            Set<V8Function> set = eventHandlerMap.get(event);
            if (set == null) {
                set = new HashSet<>();
                eventHandlerMap.put(event, set);
            }
            set.add((V8Function) v8Array.get(1));
        }, "on");
        page.registerJavaMethod((v8Object, v8Array) -> {
            String event = v8Array.getString(0);
            Set<V8Function> set = eventHandlerMap.get(event);
            if (set != null) {
                V8Function func = (V8Function) v8Array.get(1);
                Iterator<V8Function> it = set.iterator();
                while (it.hasNext()) {
                    V8Function next = it.next();
                    if (func.equals(next)) {
                        it.remove();
                        next.close();
                        break;
                    }
                }
                func.close();
            }
        }, "off");
        page.registerJavaMethod((v8Object, v8Array) -> {
            String url = v8Array.getString(0);
            V8Object paramsObj = v8Array.getObject(1);
            Map<String, ? super Object> stringMap = V8ObjectUtils.toMap(paramsObj);
            String paramJson = new JSONObject(stringMap).toString();
            paramsObj.close();

            String realUrl;
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = pageUri.schema + "://" + pageUri.host + ":" + pageUri.port + "/" + url;  // url: /Abc.js
            }
            if (!url.endsWith(".js")) {
                realUrl = url + ".js";
            } else {
                realUrl = url;
            }

            mPageContext.updateUI(() -> Orange.getInstance().getRouter().handle(context, pageUri, realUrl, paramJson));
        }, "navigate");
        page.registerJavaMethod((v8Object, v8Array) -> {

            String resultJson;
            double type = v8Array.getType(0);
            if (type == V8Object.V8_OBJECT) {
                V8Object object = v8Array.getObject(0);
                JSONObject jsonArray = new JSONObject(V8ObjectUtils.toMap(object));
                object.close();
                resultJson = jsonArray.toString();
            } else if (v8Array.length() > 0) {
                throw new IllegalArgumentException("back function accepts json an object argument or no args");
            } else {
                resultJson = "{}";
            }

            mPageContext.updateUI(() -> view.finishActivity(resultJson));
        }, "back");

        V8Object uri = createUri(v8, pageUri);

        page.add("param", param);
        page.add("uri", uri);
        page.add("view", jsRootNode);

        v8.add("page", page);

        uri.close();
        page.close();
    }

    public void setResult(V8Object result) {
        V8Object page = mPageContext.getV8().getObject("page");
        page.add("result", result);
        page.close();
    }

    private V8Object createUri(V8 v8, PageUri pageUri) {
        V8Object uri = new V8Object(v8);
        uri.add("url", pageUri.url);
        uri.add("host", pageUri.host);
        uri.add("schema", pageUri.schema);
        uri.add("port", pageUri.port);
        return uri;
    }

    public void callEventHandlers(String event) {
        Set<V8Function> set = eventHandlerMap.get(event);
        if (set != null) {
            for (V8Function f : set) {
                f.call(null, null);
            }
        }
    }

    public void release() {
        for (Set<V8Function> set : eventHandlerMap.values()) {
            for (V8Function function : set) {
                function.close();
            }
        }
    }
}
