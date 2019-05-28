package com.southstate.orange;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.southstate.orange.annotation.JSField;
import com.southstate.orange.annotation.JSFunction;
import com.southstate.orange.annotation.JSModule;
import com.southstate.orange.dom.NodeFactory;
import com.southstate.orange.dom.impl.EditNode;
import com.southstate.orange.dom.impl.FrameNode;
import com.southstate.orange.dom.impl.ImageNode;
import com.southstate.orange.dom.impl.LinearNode;
import com.southstate.orange.dom.impl.ListNode;
import com.southstate.orange.dom.impl.RefreshNode;
import com.southstate.orange.dom.impl.ScrollNode;
import com.southstate.orange.dom.impl.TextNode;
import com.southstate.orange.dom.impl.ViewNode;
import com.southstate.orange.util.PageUri;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
public final class Orange {

    private static Orange sInstance;

    private final Router mRouter;
    private final Map<String, NodeFactory.NodeCreator> mNodeCreatorMap;
    private final Map<String, ModuleDescriptor> mModulesMap;

    private final Context mContext;
    private final float mDotDensity;

    public static void init(Builder builder) {
        if (sInstance == null) {
            sInstance = new Orange(builder);
        }
    }

    private Orange(Builder builder) {
        if (builder.mContext == null) {
            throw new IllegalArgumentException("context null");
        }
        mContext = builder.mContext.getApplicationContext();
        mRouter = builder.mRouter != null ? builder.mRouter : new DefaultRouter();

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        builder.mDesignWidth = builder.mDesignWidth > 0 ? builder.mDesignWidth : 375;
        mDotDensity = dm.widthPixels * 1f / builder.mDesignWidth;

        mNodeCreatorMap = new HashMap<>();
        registerNodeCreators(builder);

        mModulesMap = new HashMap<>();
        registerModules(builder);

    }

    private void registerModules(Builder builder) {
        for (Class<?> moduleClass : builder.mModuleClasses) {
            JSModule annotation = moduleClass.getAnnotation(JSModule.class);
            if (annotation == null) {
                throw new IllegalArgumentException(moduleClass + " is not annotated by JSModule");
            }
            String moduleName = annotation.value();

            Map<String, Method> moduleFunctions = new HashMap<>();
            for (Method method : moduleClass.getMethods()) {
                JSFunction anno = method.getAnnotation(JSFunction.class);
                if (anno != null) {
                    Class<?> type = method.getReturnType();
                    if (int.class == type
                            || Integer.class == type
                            || double.class == type
                            || Double.class == type
                            || String.class == type
                            || Boolean.class == type
                            || boolean.class == type
                            || V8Value.class.isAssignableFrom(type)) {
                        String funcName = anno.value();
                        moduleFunctions.put(funcName, method);
                    } else {
                        throw new IllegalArgumentException("JSFunction必须返回int，double，String，boolean或V8Value");
                    }
                }
            }

            Map<String, Field> moduleFields = new HashMap<>();
            for (Field field : moduleClass.getFields()) {
                JSField anno = field.getAnnotation(JSField.class);
                if (anno != null) {
                    Class<?> type = field.getType();
                    if (int.class == type
                            || Integer.class == type
                            || double.class == type
                            || Double.class == type
                            || String.class == type
                            || Boolean.class == type
                            || boolean.class == type) {
                        String fieldName = anno.value();
                        moduleFields.put(fieldName, field);
                    } else {
                        throw new IllegalArgumentException("JSField必须是int，double，string或boolean");
                    }
                }
            }

            ModuleDescriptor descriptor = new ModuleDescriptor();
            descriptor.moduleClass = moduleClass;
            descriptor.methodMap = moduleFunctions;
            descriptor.fieldMap = moduleFields;
            mModulesMap.put(moduleName, descriptor);
        }
    }

    private void registerNodeCreators(Builder builder) {
        mNodeCreatorMap.put("view", ViewNode::new);
        mNodeCreatorMap.put("text", TextNode::new);
        mNodeCreatorMap.put("edit", EditNode::new);
        mNodeCreatorMap.put("image", ImageNode::new);
        mNodeCreatorMap.put("frame", FrameNode::new);
        mNodeCreatorMap.put("linear", LinearNode::new);
        mNodeCreatorMap.put("scroll", ScrollNode::new);
        mNodeCreatorMap.put("list", ListNode::new);
        mNodeCreatorMap.put("refresh", RefreshNode::new);
        for (Map.Entry<String, NodeFactory.NodeCreator> entry : builder.mNodeCreatorMap.entrySet()) {
            mNodeCreatorMap.put(entry.getKey(), entry.getValue());
        }
    }

    public Router getRouter() {
        return mRouter;
    }

    public NodeFactory.NodeCreator getNodeCreator(String viewTag) {
        return mNodeCreatorMap.get(viewTag);
    }

    public static Orange getInstance() {
        return sInstance;
    }

    public float getDotDensity() {
        return mDotDensity;
    }

    @Nullable
    public V8Object createModule(V8 v8, String moduleName) {
        ModuleDescriptor descriptor = mModulesMap.get(moduleName);
        if (descriptor == null) {
            return null;
        }

        V8Object module = new V8Object(v8);

        try {
            Object moduleInstance = descriptor.moduleClass.newInstance();
            // methods
            for (Map.Entry<String, Method> entry : descriptor.methodMap.entrySet()) {
                String name = entry.getKey();
                Method func = entry.getValue();
                module.registerJavaMethod((v8Object, v8Array) -> {
                    int length = func.getParameterTypes().length;
                    Object[] args = new Object[length];
                    for (int i = 0; i < length; i++) {
                        if (v8Array.getType(i) == V8Value.UNDEFINED) {
                            args[i] = null;
                        } else {
                            args[i] = v8Array.get(i);
                        }
                    }
                    try {
                        return func.invoke(moduleInstance, args);
                    } catch (Exception e) {
                        throw new RuntimeException("执行模块方法" + moduleName + "." + name + "出错", e);
                    }
                }, name);
            }
            // fields
            for (Map.Entry<String, Field> entry : descriptor.fieldMap.entrySet()) {
                String name = entry.getKey();
                Field field = entry.getValue();
                Object val = field.get(moduleInstance);
                if (val == null) {
                    module.add(name, (String) null);
                } else if (val instanceof Integer) {
                    module.add(name, (Integer) val);
                } else if (val instanceof Double) {
                    module.add(name, (Double) val);
                } else if (val instanceof String) {
                    module.add(name, (String) val);
                } else if (val instanceof Boolean) {
                    module.add(name, (Boolean) val);
                }
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return module;
    }

    public Context getContext() {
        return mContext;
    }

    public interface Router {
        void handle(Context context, PageUri currPage, String targetUrl, String paramJson);
    }

    private static class DefaultRouter implements Router {

        @Override
        public void handle(Context context, PageUri currPage, String targetUrl, String paramJson) {
            OrangeActivity.launch(context, targetUrl, paramJson);
        }
    }

    public static class Builder {

        private Context mContext;
        private int mDesignWidth;
        private Router mRouter;
        private Map<String, NodeFactory.NodeCreator> mNodeCreatorMap = new HashMap<>(0);
        private Set<Class<?>> mModuleClasses = new HashSet<>();

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder designWidth(int designWidth) {
            mDesignWidth = designWidth;
            return this;
        }

        public Builder router(Router router) {
            mRouter = router;
            return this;
        }

        public Builder registerView(String nodeName, NodeFactory.NodeCreator nodeCreator) {
            mNodeCreatorMap.put(nodeName, nodeCreator);
            return this;
        }

        public Builder registerModule(Class<?> moduleClass) {
            mModuleClasses.add(moduleClass);
            return this;
        }
    }

    private class ModuleDescriptor {
        Class<?> moduleClass;
        Map<String, Method> methodMap;
        Map<String, Field> fieldMap;
    }
}
