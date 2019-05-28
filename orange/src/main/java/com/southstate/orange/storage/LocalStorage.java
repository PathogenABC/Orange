package com.southstate.orange.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.LruCache;

import com.southstate.orange.BuildConfig;
import com.southstate.orange.Orange;
import com.southstate.orange.util.JsonUtil;
import com.southstate.orange.util.Undefined;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by junkang on 2019/5/28.
 * Function: Orange
 */
public final class LocalStorage implements IStorage {

    private static final LruCache<String, LocalStorage> MAP = new LruCache<>(5);

    public static LocalStorage of(String namespace) {
        LocalStorage storage = MAP.get(namespace);
        if (storage != null) {
            return storage;
        }
        synchronized (MAP) {
            storage = MAP.get(namespace);
            if (storage != null) {
                return storage;
            }
            storage = new LocalStorage(namespace);
            MAP.put(namespace, storage);
            return storage;
        }
    }

    private final SharedPreferences mPreferences;

    private LocalStorage(String namespace) {
        if (BuildConfig.DEBUG) {
            namespace = "debug_" + namespace;
        } else {
            namespace = "release_" + namespace;
        }
        mPreferences = Orange.getInstance().getContext().getSharedPreferences(namespace, Context.MODE_PRIVATE);
    }

    @Override
    public synchronized String getString(String key) {
        return (String) getItem(key);
    }

    @Override
    public synchronized int getInt(String key) {
        return (int) getItem(key);
    }

    @Override
    public synchronized float getFloat(String key) {
        return (float) getItem(key);
    }

    @Override
    public synchronized double getDouble(String key) {
        return (double) getItem(key);
    }

    @Override
    public synchronized boolean getBoolean(String key) {
        return (boolean) getItem(key);
    }

    @Override
    public synchronized Map<String, Object> getObject(String key) {
        //noinspection unchecked
        return (Map<String, Object>) getItem(key);
    }

    @Override
    public synchronized List<Object> getArray(String key) {
        //noinspection unchecked
        return (List<Object>) getItem(key);
    }

    @Override
    public synchronized boolean hasKey(String key) {
        return mPreferences.contains(key);
    }

    @Override
    public synchronized Object getItem(String key) {
        String value = mPreferences.getString(key, "Undefined");
        if (value == null) {
            return null;
        }
        if ("Null".equals(value)) {
            return null;
        }
        if (value.startsWith("Boolean_")) {
            return Boolean.parseBoolean(value.substring("Boolean_".length()));
        }
        if (value.startsWith("String_")) {
            return value.substring("String_".length());
        }
        if (value.startsWith("Float_")) {
            return Float.parseFloat(value.substring("Float_".length()));
        }
        if (value.startsWith("Double_")) {
            return Double.parseDouble(value.substring("Double_".length()));
        }
        if (value.startsWith("Object_")) {
            return JsonUtil.getMapForJson(value.substring("Object_".length()));
        }
        if (value.startsWith("Array_")) {
            return JsonUtil.getListForJson(value.substring("Array_".length()));
        }
        return Undefined.INSTANCE;
    }

    @Override
    public synchronized void removeItem(String key) {
        mPreferences.edit().remove(key).apply();
    }

    @Override
    public synchronized void setItem(String key, Object value) {
        if (value instanceof Boolean) {
            mPreferences.edit().putString(key, "Boolean_" + value).apply();
        } else if (value instanceof String) {
            mPreferences.edit().putString(key, "String_" + value).apply();
        } else if (value instanceof Float) {
            mPreferences.edit().putString(key, "Float_" + value).apply();
        } else if (value instanceof Double) {
            mPreferences.edit().putString(key, "Double_" + value).apply();
        } else if (value instanceof Map) {
            mPreferences.edit().putString(key, "Object_" + new JSONObject((Map) value).toString()).apply();
        } else if (value instanceof List) {
            mPreferences.edit().putString(key, "Array_" + new JSONArray((List) value).toString()).apply();
        } else if (value == Undefined.INSTANCE) {
            mPreferences.edit().putString(key, "Undefined").apply();
        } else {
            mPreferences.edit().putString(key, "Null").apply();
        }
    }

    @Override
    public synchronized void clear() {
        mPreferences.edit().clear().apply();
    }
}
