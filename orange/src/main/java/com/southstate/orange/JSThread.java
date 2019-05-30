package com.southstate.orange;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.NodeFactory;
import com.southstate.orange.dom.RootNode;
import com.southstate.orange.lib.LibApp;
import com.southstate.orange.lib.LibConsole;
import com.southstate.orange.lib.LibHttp;
import com.southstate.orange.lib.LibModule;
import com.southstate.orange.lib.LibPage;
import com.southstate.orange.lib.LibStorage;
import com.southstate.orange.lib.LibTimer;
import com.southstate.orange.lib.LibUI;
import com.southstate.orange.util.ErrorUtil;
import com.southstate.orange.util.JsonUtil;
import com.southstate.orange.util.PageUri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
public class JSThread extends HandlerThread {

    private static int nameIndex;

    private final OrangeView mOrangeView;
    private final PageContext mPageContext;
    private Handler mJsHandler;
    private Handler mUiHandler;
    private V8 v8;
    private LibPage mPage;
    private final List<Runnable> uiUpdateQueueInAFrame = new ArrayList<>();

    private RootNode mRootNode;

    JSThread(OrangeView orangeView) {
        super("js-thread-" + nameIndex++);
        this.mOrangeView = orangeView;
        this.mPageContext = new PageContext(this);
        this.mUiHandler = new Handler(Looper.getMainLooper(), msg -> {
            while (uiUpdateQueueInAFrame.size() > 0) {
                ArrayList<Runnable> copy;
                synchronized (uiUpdateQueueInAFrame) {
                    copy = new ArrayList<>(uiUpdateQueueInAFrame);
                    uiUpdateQueueInAFrame.clear();
                }
                for (Runnable runnable : copy) {
                    runnable.run();
                }
                Log.d("js-thread", "run all in one : " + Thread.currentThread().getName());
            }
            return true;
        });
        this.mPage = new LibPage(mPageContext);
    }

    @Override
    public void run() {
        setDefaultUncaughtExceptionHandler((t, e) -> onThreadEnd(e));
        initV8Runtime();

        super.run();
        onThreadEnd(null);
    }

    private void initV8Runtime() {
        v8 = V8.createV8Runtime();
    }

    private void onThreadEnd(Throwable e) {
        Log.d(getName(), "exception", e);

        NodeFactory.release();
        LibModule.release();
        if (v8 != null) {
            v8.release(false);
            v8 = null;
        }
    }

    @NonNull
    public Handler getHandler() {
        if (mJsHandler == null) {
            mJsHandler = new Handler(getLooper());
        }
        return mJsHandler;
    }

    public V8 getV8() {
        getLooper();
        return v8;
    }

    public Context getContext() {
        return mOrangeView.getContext();
    }

    public OrangeView getView() {
        return mOrangeView;
    }

    void installLibsContext(PageUri pageUri, Map<String, Object> entryJsParams) {
        V8Object params = V8ObjectUtils.toV8Object(v8, entryJsParams);

        V8Object jsRootNode = new V8Object(v8);
        mRootNode = new RootNode(mPageContext, jsRootNode);

        mPage.install(pageUri, jsRootNode, params);

        jsRootNode.close();
        params.close();

        PageContext pc = this.mPageContext;
        LibUI.install(pc);
        LibConsole.install(pc);
        LibApp.install(pc);
        LibTimer.install(pc);
        LibHttp.install(pc);
        LibStorage.install(pc);
        LibModule.install(pc);
    }

    void callEventHandlers(String event) {
        mPage.callEventHandlers(event);
    }

    void setPageResult(String resultJson) {
        V8Object result = V8ObjectUtils.toV8Object(v8, JsonUtil.getMapForJson(resultJson != null ? resultJson : "{}"));
        mPage.setResult(result);
        result.close();
    }

    public void updateJS(Runnable runnable) {
        getHandler().post(() -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                ErrorUtil.handleJsError(getContext(), e);
            }
        });
    }

    public void updateUI(Runnable uiRunnable) {
        Log.d("js-thread", "runnable = " + uiRunnable);
        synchronized (uiUpdateQueueInAFrame) {
            uiUpdateQueueInAFrame.add(() -> {
                try {
                    uiRunnable.run();
                } catch (Throwable e) {
                    ErrorUtil.handleJsError(JSThread.this.getContext(), e);
                }
            });
        }
        if (!mUiHandler.hasMessages(0)) {
            mUiHandler.sendEmptyMessage(0);
        }
    }

    void attachPageToView() {
        mRootNode.addToView();
    }

    void detachPageFromView() {
        mRootNode.removeFromView();
        mRootNode = null;
    }
}
