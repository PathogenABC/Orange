package com.southstate.orange.storage;

import java.util.List;
import java.util.Map;

/**
 * Created by junkang on 2019/5/28.
 * Function: Orange
 */
public interface IStorage {

    String getString(String key);

    int getInt(String key);

    float getFloat(String key);

    double getDouble(String key);

    boolean getBoolean(String key);

    Map<String, Object> getObject(String key);

    List<Object> getArray(String key);

    boolean hasKey(String key);

    Object getItem(String key);

    void removeItem(String key);

    void setItem(String key, Object value);

    void clear();
}
