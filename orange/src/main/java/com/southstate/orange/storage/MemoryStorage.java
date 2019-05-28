package com.southstate.orange.storage;

import com.southstate.orange.util.Undefined;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junkang on 2019/5/28.
 * Function: Orange
 */
public final class MemoryStorage implements IStorage {

    private static final Map<String, MemoryStorage> MAP = new HashMap<>();

    public static MemoryStorage of(String namespace) {
        MemoryStorage storage = MAP.get(namespace);
        if (storage != null) {
            return storage;
        }
        synchronized (MAP) {
            storage = MAP.get(namespace);
            if (storage != null) {
                return storage;
            }
            storage = new MemoryStorage();
            MAP.put(namespace, storage);
            return storage;
        }
    }

    private final HashMap<String, Object> mValues = new HashMap<>();

    private MemoryStorage() {
    }

    @Override
    public synchronized String getString(String key) {
        return (String) mValues.get(key);
    }

    @Override
    public synchronized int getInt(String key) {
        return (int) mValues.get(key);
    }

    @Override
    public synchronized float getFloat(String key) {
        return (float) mValues.get(key);
    }

    @Override
    public synchronized double getDouble(String key) {
        return (double) mValues.get(key);
    }

    @Override
    public synchronized boolean getBoolean(String key) {
        return (boolean) mValues.get(key);
    }

    @Override
    public synchronized Map<String, Object> getObject(String key) {
        //noinspection unchecked
        return (Map<String, Object>) mValues.get(key);
    }

    @Override
    public synchronized List<Object> getArray(String key) {
        //noinspection unchecked
        return (List<Object>) mValues.get(key);
    }

    @Override
    public synchronized boolean hasKey(String key) {
        return mValues.containsKey(key);
    }

    @Override
    public synchronized Object getItem(String key) {
        if (mValues.containsKey(key)) {
            return mValues.get(key);
        }
        return Undefined.INSTANCE;
    }

    @Override
    public synchronized void removeItem(String key) {
        mValues.remove(key);
    }

    @Override
    public void setItem(String key, Object value) {
        if (value != null
                && !(value instanceof Boolean)
                && !(value instanceof String)
                && !(value instanceof Integer)
                && !(value instanceof Float)
                && !(value instanceof Double)
                && !(value instanceof Map)
                && !(value instanceof List)) {
            throw new IllegalArgumentException("不接受[" + value.getClass().getSimpleName() + "]类型的参数");
        }
        synchronized (this) {
            mValues.put(key, value);
        }
    }

    @Override
    public synchronized void clear() {
        mValues.clear();
    }

}
