package com.southstate.orange.dom;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.util.V8Util;
import com.southstate.orange.util.ViewUtil;
import com.southstate.orange.view.IView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.southstate.orange.util.DimenUtil.dot;
import static com.southstate.orange.util.DimenUtil.px;
import static com.southstate.orange.util.V8Util.asBool;
import static com.southstate.orange.util.V8Util.asColor;
import static com.southstate.orange.util.V8Util.asFloat;
import static com.southstate.orange.util.V8Util.asFloatOfPercent;
import static com.southstate.orange.util.V8Util.asLayoutSize;
import static com.southstate.orange.util.V8Util.asVisibility;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
public abstract class Node<T extends View> {

    private PageContext mPageContext;

    private Map<String, Object> mAttributes = new HashMap<>();
    private Map<String, Object> mDirtyAttributes = new HashMap<>();

    private T mView;
    private IView mIView;

    GroupNode<?> mParent;
    boolean mAttachedToWindow;

    private float mWidth = 0;
    private float mHeight = 0;

    String mUniqueRef;

    public Node(PageContext pc, V8Object jsNode) {
        mPageContext = pc;

        mView = createView(pc.getContext());
        mIView = (IView) mView;
        onConfigureJSNode(jsNode);
        updateUI(this::onConfigureView);
    }

    @CallSuper
    protected void onConfigureJSNode(V8Object jsNode) {
        jsNode.registerJavaMethod(new SetAttributeFunc(), "setAttribute");
        jsNode.registerJavaMethod(new GetAttributeFunc(), "getAttribute");

        jsNode.registerJavaMethod((JavaCallback) (v8Object, v8Array) -> mWidth, "getWidth");
        jsNode.registerJavaMethod((JavaCallback) (v8Object, v8Array) -> mHeight, "getHeight");
    }

    @UiThread
    @CallSuper
    protected void onConfigureView() {
        mIView.setOnSizeChangeListener((width, height) -> updateJS(() -> {
            float dw = dot(width);
            float dh = dot(height);
            mWidth = dw;
            mHeight = dh;

            Object func = getAttribute("onSizeChanged");
            if (func instanceof V8Function) {
                V8Array param = V8Util.buildParam(mPageContext.getV8(), dw, dh);
                ((V8Function) func).call(null, param);
            }
        }));
    }

    protected abstract T createView(Context context);

    public void attachToWindow() {
        mAttachedToWindow = true;
        updateDirtyAttributes();
    }

    public void detachFromWindow() {
        mAttachedToWindow = false;
    }

    @CallSuper
    protected boolean onUpdateAttribute(String attrName, Object twinAttrVal) {
        switch (attrName) {
            case "visibility": {
                int visibility = asVisibility(twinAttrVal, "visibility设置错误");
                updateUI(() -> mView.setVisibility(visibility));
                if (visibility != View.GONE) {
                    updateDirtyAttributes();
                }
                break;
            }
            case "layoutWidth":
                int w = asLayoutSize(twinAttrVal, "layoutWidth须为wrapContent，matchParent或数字值");
                updateUI(() -> ViewUtil.updateLayoutParams(mView, lp -> lp.width = w));
                break;
            case "layoutHeight":
                int h = asLayoutSize(twinAttrVal, "layoutHeight须为wrapContent，matchParent或数字值");
                updateUI(() -> ViewUtil.updateLayoutParams(mView, lp -> lp.height = h));
                break;
            case "padding": {
                int val = (int) px(asFloat(twinAttrVal, "padding须是数字值"));
                updateUI(() -> ViewUtil.updatePadding(mView, padding -> {
                    padding.left = val;
                    padding.top = val;
                    padding.right = val;
                    padding.bottom = val;
                }));
                break;
            }
            case "paddingLeft": {
                int val = (int) px(asFloat(twinAttrVal, "paddingLeft须是数字值"));
                updateUI(() -> ViewUtil.updatePadding(mView, padding -> padding.left = val));
                break;
            }
            case "paddingTop": {
                int val = (int) px(asFloat(twinAttrVal, "paddingTop须是数字值"));
                updateUI(() -> ViewUtil.updatePadding(mView, padding -> padding.top = val));
                break;
            }
            case "paddingRight": {
                int val = (int) px(asFloat(twinAttrVal, "paddingRight须是数字值"));
                updateUI(() -> ViewUtil.updatePadding(mView, padding -> padding.right = val));
                break;
            }
            case "paddingBottom": {
                int val = (int) px(asFloat(twinAttrVal, "paddingBottom须是数字值"));
                updateUI(() -> ViewUtil.updatePadding(mView, padding -> padding.bottom = val));
                break;
            }
            case "margin": {
                int val = (int) px(asFloat(twinAttrVal, "margin须是数字值"));
                updateUI(() -> ViewUtil.updateLayoutParams(mView, lp -> {
                    lp.leftMargin = val;
                    lp.topMargin = val;
                    lp.rightMargin = val;
                    lp.bottomMargin = val;
                }));
                break;
            }
            case "marginLeft": {
                int val = (int) px(asFloat(twinAttrVal, "marginLeft须是数字值"));
                updateUI(() -> ViewUtil.updateLayoutParams(mView, lp -> lp.leftMargin = val));
                break;
            }
            case "marginTop": {
                int val = (int) px(asFloat(twinAttrVal, "marginTop须是数字值"));
                updateUI(() -> ViewUtil.updateLayoutParams(mView, lp -> lp.topMargin = val));
                break;
            }
            case "marginRight": {
                int val = (int) px(asFloat(twinAttrVal, "marginRight须是数字值"));
                updateUI(() -> ViewUtil.updateLayoutParams(mView, lp -> lp.rightMargin = val));
                break;
            }
            case "marginBottom": {
                int val = (int) px(asFloat(twinAttrVal, "marginBottom须是数字值"));
                updateUI(() -> ViewUtil.updateLayoutParams(mView, lp -> lp.bottomMargin = val));
                break;
            }
            case "backgroundColor":
                int backgroundColor = asColor(twinAttrVal, "backgroundColor须为颜色值");
                updateUI(() -> mView.setBackgroundColor(backgroundColor));
                break;
            case "borderWidth":
                if (mIView.getBorder() != null) {
                    int borderWidth = (int) px(asFloat(twinAttrVal, "borderWidth须为数字值"));
                    updateUI(() -> mIView.getBorder().setBorderWidth(borderWidth));
                }
                break;
            case "borderColor":
                if (mIView.getBorder() != null) {
                    int borderColor = asColor(twinAttrVal, "borderColor须为颜色值");
                    updateUI(() -> mIView.getBorder().setBorderColor(borderColor));
                }
                break;
            case "borderRadius":
                if (mIView.getBorder() != null) {
                    if (twinAttrVal instanceof String) {
                        float percent = asFloatOfPercent((String) twinAttrVal, "borderRadius须为百分比字符串或数字值");
                        updateUI(() -> mIView.getBorder().setBorderRadius(percent, true));
                    } else if (twinAttrVal instanceof Number) {
                        float bRpx = px(((Number) twinAttrVal).floatValue());
                        updateUI(() -> mIView.getBorder().setBorderRadius(bRpx, false));
                    }
                }
                break;
            case "onSizeChanged":
                // do nothing
                break;
            case "onClick":
                if (twinAttrVal instanceof V8Function) {
                    updateUI(() -> mView.setOnClickListener(v -> updateJS(() -> {
                        V8Function func = (V8Function) getAttribute("onClick");
                        if (func != null) {
                            func.call(null, null);
                        }
                    })));
                } else {
                    updateUI(() -> mView.setOnClickListener(null));
                }
                break;
            case "onLongClick":
                if (twinAttrVal instanceof V8Function) {
                    updateUI(() -> mView.setOnLongClickListener(v -> {
                        updateJS(() -> {
                            V8Function func = (V8Function) getAttribute("onLongClick");
                            if (func != null) {
                                func.call(null, null);
                            }
                        });
                        return true;
                    }));
                } else {
                    updateUI(() -> mView.setOnLongClickListener(null));
                }
                break;
            case "clipToPadding":
                if (getView() instanceof ViewGroup) {
                    boolean clipToPadding = asBool(twinAttrVal, "clipToPadding须是bool值");
                    updateUI(() -> ((ViewGroup) getView()).setClipToPadding(clipToPadding));
                }
                break;
            default:
                return false;
        }
        return true;
    }

    protected void setAttribute(String attrName, Object attrVal) {
        if (!Objects.equals(getAttribute(attrName), attrVal)) {
            Object twinAttrVal = V8Util.twin(attrVal);

            if (!"visibility".equals(attrName) && (!mAttachedToWindow || isVisibilityGone())) {
                mDirtyAttributes.put(attrName, twinAttrVal);
                return;
            }

            onSetAttribute(attrName, twinAttrVal);

            boolean selfUpdated = onUpdateAttribute(attrName, twinAttrVal);
            if (!selfUpdated && mParent != null) {
                mParent.onUpdateChildAttribute(this, attrName, twinAttrVal);
            }
        }
        V8Util.close(attrVal);
    }

    @CallSuper
    protected void onSetAttribute(String attrName, Object twinAttrVal) {
        mAttributes.put(attrName, twinAttrVal);
    }

    private boolean isVisibilityGone() {
        return "gone".equals(String.valueOf(mAttributes.get("visibility")).trim());
    }

    protected Object getAttribute(String attrName) {
        return mAttributes.get(attrName);
    }

    private void updateDirtyAttributes() {
        HashMap<String, Object> copy = new HashMap<>(mDirtyAttributes);
        mDirtyAttributes.clear();
        for (Map.Entry<String, Object> entry : copy.entrySet()) {
            setAttribute(entry.getKey(), V8Util.twin(entry.getValue()));
        }
    }

    protected void updateJS(Runnable runnable) {
        mPageContext.updateJS(runnable);
    }

    protected void updateUI(Runnable runnable) {
        mPageContext.updateUI(runnable);
    }

    protected PageContext getPageContext() {
        return mPageContext;
    }

    public T getView() {
        return mView;
    }

    public GroupNode<?> getParent() {
        return mParent;
    }

    @CallSuper
    public void release() {
        for (Map.Entry<String, Object> e : mAttributes.entrySet()) {
            V8Util.close(e.getValue());
        }

        for (Map.Entry<String, Object> e : mDirtyAttributes.entrySet()) {
            V8Util.close(e.getValue());
        }
    }

    private class SetAttributeFunc implements JavaVoidCallback {
        @Override
        public void invoke(V8Object v8Object, V8Array v8Array) {
            if (v8Array.getType(0) != V8Value.STRING) {
                throw new IllegalArgumentException("setAttribute 第一个参数必须是String");
            }
            if (v8Array.length() < 2) {
                throw new IllegalArgumentException("setAttribute 必须传入第二个参数");
            }
            String attrName = v8Array.getString(0);
            Object attrVal = v8Array.get(1);
            setAttribute(attrName, attrVal);
        }
    }

    private class GetAttributeFunc implements JavaCallback {
        @Override
        public Object invoke(V8Object v8Object, V8Array v8Array) {
            if (v8Array.getType(0) != V8Value.STRING) {
                throw new IllegalArgumentException("setAttribute 第一个参数必须是String");
            }
            String attrName = v8Array.getString(0);
            return getAttribute(attrName);
        }
    }

}
