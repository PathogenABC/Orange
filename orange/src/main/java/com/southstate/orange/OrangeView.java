package com.southstate.orange;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.southstate.orange.util.JsonUtil;
import com.southstate.orange.util.PageUri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
@SuppressWarnings("Convert2MethodRef")
public class OrangeView extends FrameLayout {
    private JSThread mThread;
    private boolean mReadyCalled;
    private String mEntryScript;
    private Map<String, Object> mEntryParams;
    private OnLoadProgressListener mProgressListener;
    private List<OnSizeChangeListener> mSizeChangeListeners = new ArrayList<>();
    private ScriptLoadTask mCurrScriptLoader;
    private String mUrl;
    private OnFinishActivityListener mOnFinishActivityListener;

    public OrangeView(Context context) {
        this(context, null);
    }

    public OrangeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    public void setOnLoadProgressListener(OnLoadProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }

    public void addOnSizeChangeListener(OnSizeChangeListener sizeChangeListener) {
        this.mSizeChangeListeners.add(sizeChangeListener);
    }

    public void setOnFinishActivityListener(OnFinishActivityListener onFinishActivityListener) {
        this.mOnFinishActivityListener = onFinishActivityListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (OnSizeChangeListener listener : mSizeChangeListeners) {
            listener.onSizeChange(w, h);
        }
    }

    public void loadUrl(String url, String params) {
        cancelPreviousLoading();
        onHide();
        onClose();

        Runnable r = () -> {
            mThread = new JSThread(this);
            mThread.start();

            mCurrScriptLoader = new ScriptLoadTask(url, (js, error) -> {
                if (mProgressListener != null) {
                    mProgressListener.onLoadEnd(error);
                }
                if (js != null && error == null) {
                    mEntryScript = js;
                    mEntryParams = params == null ? Collections.EMPTY_MAP : JsonUtil.getMapForJson(params);
                    this.mUrl = url;
                    onReady();
                    onShow();
                }
            });
            mCurrScriptLoader.load();
        };

        if (getWidth() == 0) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    r.run();
                    return false;
                }
            });
        } else {
            r.run();
        }

        if (mProgressListener != null) {
            mProgressListener.onLoadStart();
        }
    }

    private void cancelPreviousLoading() {
        if (mProgressListener != null) {
            mProgressListener.onLoadEnd(null);
        }

        if (mCurrScriptLoader != null) {
            mCurrScriptLoader.cancel();
            mCurrScriptLoader = null;
        }
    }

    private void updateJS(Runnable runnable) {
        mThread.updateJS(runnable);
    }

    private void onReady() {
        if (!mReadyCalled) {
            mReadyCalled = true;
            JSThread jsThread = this.mThread;
            updateJS(() -> jsThread.installLibsContext(new PageUri(mUrl), mEntryParams));
            updateJS(() -> jsThread.getV8().executeVoidScript(mEntryScript));
            updateJS(() -> jsThread.callEventHandlers("ready"));
            updateJS(() -> jsThread.attachPageToView());
        }
    }

    public void onClose() {
        if (mReadyCalled) {
            mReadyCalled = false;
            JSThread jsThread = this.mThread;
            // 先remove，确保"移出JS事件"的正确执行
            updateJS(() -> jsThread.detachPageFromView());
            updateJS(() -> jsThread.callEventHandlers("close"));
            updateJS(() -> jsThread.quitSafely());
            this.mThread = null;
        }
    }

    public void onShow() {
        if (mReadyCalled) {
            JSThread jsThread = this.mThread;
            updateJS(() -> jsThread.callEventHandlers("show"));
        }
    }

    public void onHide() {
        if (mReadyCalled) {
            JSThread jsThread = this.mThread;
            updateJS(() -> jsThread.callEventHandlers("hide"));
        }
    }

    public void finishActivity(String resultJson) {
        if (mOnFinishActivityListener != null) {
            mOnFinishActivityListener.onFinishActivity(resultJson);
        }
    }

    public void setPageResult(String resultJson) {
        updateJS(() -> mThread.setPageResult(resultJson));
    }

    public interface OnLoadProgressListener {
        void onLoadStart();

        void onLoadEnd(Throwable error);
    }

    public interface OnSizeChangeListener {
        void onSizeChange(int width, int height);
    }

    public interface OnFinishActivityListener {
        void onFinishActivity(String resultJson);
    }
}
