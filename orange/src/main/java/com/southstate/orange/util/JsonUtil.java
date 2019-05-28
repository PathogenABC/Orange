package com.southstate.orange.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
public class JsonUtil {

    public static Map<String, Object> getMapForJson(String jsonStr) {
        try {
            return getMapForJson(new JSONObject(jsonStr));
        } catch (JSONException e) {
            return Collections.emptyMap();
        }
    }

    public static Map<String, Object> getMapForJson(JSONObject jsonObject) {
        Map<String, Object> valueMap = new HashMap<>();

        try {
            Iterator<String> keyIt = jsonObject.keys();
            String key;
            Object value;
            while (keyIt.hasNext()) {
                key = keyIt.next();
                value = jsonObject.get(key);

                if (value instanceof JSONObject) {
                    valueMap.put(key, getMapForJson((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    valueMap.put(key, getListForJson((JSONArray) value));
                } else {
                    valueMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueMap;
    }

    public static List<Object> getListForJson(String jsonStr) {
        try {
            return getListForJson(new JSONArray(jsonStr));
        } catch (JSONException e) {
            return Collections.emptyList();
        }
    }

    public static List<Object> getListForJson(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object o = jsonArray.get(i);
                if (o instanceof JSONObject) {
                    list.add(getMapForJson((JSONObject) o));
                } else if (o instanceof JSONArray) {
                    list.add(getListForJson((JSONArray) o));
                } else {
                    list.add(o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
