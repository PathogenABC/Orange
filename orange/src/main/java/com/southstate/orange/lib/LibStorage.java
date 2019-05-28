package com.southstate.orange.lib;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.storage.IStorage;
import com.southstate.orange.storage.LocalStorage;
import com.southstate.orange.storage.MemoryStorage;
import com.southstate.orange.util.Undefined;

import java.util.List;
import java.util.Map;

/**
 * Created by junkang on 2019/5/28.
 * Function: Orange
 */
public class LibStorage {

    public static void install(PageContext pc) {
        V8 v8 = pc.getV8();
        registerMemoryStorage(v8);
        registerLocalStorage(v8);
    }

    private static void registerMemoryStorage(V8 v8) {
        // memory storage
        V8Object memoryStorage = new V8Object(v8);

        memoryStorage.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) != V8Value.STRING) {
                throw new IllegalArgumentException("of接受一个string类型的namespace");
            }
            String namespace = v8Array.getString(0);
            return createInstance(v8, namespace, MemoryStorage::of);
        }, "of");

        v8.add("memoryStorage", memoryStorage);
        memoryStorage.close();
    }

    private static void registerLocalStorage(V8 v8) {
        // local storage
        V8Object localStorage = new V8Object(v8);

        localStorage.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) != V8Value.STRING) {
                throw new IllegalArgumentException("of接受一个string类型的namespace");
            }
            String namespace = v8Array.getString(0);
            return createInstance(v8, namespace, LocalStorage::of);
        }, "of");

        v8.add("localStorage", localStorage);
        localStorage.close();
    }

    private static V8Object createInstance(V8 v8, String namespace, StorageGetter getter) {
        V8Object instance = new V8Object(v8);
        instance.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) != V8Value.STRING) {
                throw new IllegalArgumentException("key必须是string");
            }
            if (v8Array.length() != 2) {
                throw new IllegalArgumentException("只接受2个参数");
            }
            String key = v8Array.getString(0);
            Object value;
            switch (v8Array.getType(1)) {
                case V8Value.NULL:
                case V8Value.INTEGER:
                case V8Value.DOUBLE:
                case V8Value.BOOLEAN:
                case V8Value.STRING:
                    value = v8Array.get(1);
                    break;
                case V8Value.BYTE:
                    value = ((Number) v8Array.get(1)).intValue();
                    break;
                case V8Value.V8_OBJECT:
                    V8Object object = v8Array.getObject(1);
                    value = V8ObjectUtils.toMap(object);
                    object.close();
                    break;
                case V8Value.V8_ARRAY:
                    V8Array array = v8Array.getArray(1);
                    value = V8ObjectUtils.toList(array);
                    array.close();
                    break;
                default:
                    value = Undefined.INSTANCE;
            }
            getter.getStorage(namespace).setItem(key, value);
        }, "setItem");

        instance.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) != V8Value.STRING) {
                throw new IllegalArgumentException("key必须是string");
            }
            String key = v8Array.getString(0);
            IStorage storage = getter.getStorage(namespace);

            Object item = storage.getItem(key);
            if (item instanceof Map) {
                //noinspection unchecked
                item = V8ObjectUtils.toV8Object(v8, (Map<String, ?>) item);
            } else if (item instanceof List) {
                item = V8ObjectUtils.toV8Array(v8, (List<?>) item);
            } else if (item == Undefined.INSTANCE) {
                V8Object tmp = new V8Object(v8);
                tmp.addUndefined("_tmp");
                item = tmp.get("_tmp");
                tmp.close();
            }
            return item;
        }, "getItem");

        instance.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) != V8Value.STRING) {
                throw new IllegalArgumentException("key必须是string");
            }
            String key = v8Array.getString(0);
            getter.getStorage(namespace).removeItem(key);
        }, "removeItem");

        instance.registerJavaMethod((v8Object, v8Array) -> {
            getter.getStorage(namespace).clear();
        }, "clear");
        return instance;
    }

    interface StorageGetter {
        IStorage getStorage(String namespace);
    }
}
