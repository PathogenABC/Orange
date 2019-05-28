package com.southstate.orange.dom.impl;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.GroupNode;
import com.southstate.orange.util.V8Util;
import com.southstate.orange.view.Refresh;

import static com.southstate.orange.util.V8Util.asBool;

/**
 * Created by junkang on 2019/5/20.
 * Function: Orange
 */
public class RefreshNode extends GroupNode<Refresh> {

    public RefreshNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @Override
    protected Refresh createView(Context context) {
        return new Refresh(context);
    }

    @Override
    protected void onConfigureView() {
        super.onConfigureView();
        Refresh view = getView();
        view.setRefreshHeader(new ClassicsHeader(getPageContext().getContext()));
        view.setRefreshFooter(new ClassicsFooter(getPageContext().getContext()));
        view.setEnableOverScrollDrag(true);
        view.setEnableAutoLoadMore(false);
        view.setOnRefreshListener(refreshLayout -> updateJS(() -> {
            Object onRefresh = getAttribute("onRefresh");
            if (onRefresh instanceof V8Function) {
                V8Array parameters = V8Util.buildParam(getPageContext().getV8(), "refresh");
                ((V8Function) onRefresh).call(null, parameters);
            }
        }));
        view.setOnLoadMoreListener(refreshLayout -> updateJS(() -> {
            Object onLoadmore = getAttribute("onLoadmore");
            if (onLoadmore instanceof V8Function) {
                V8Array parameters = V8Util.buildParam(getPageContext().getV8(), "loadmore");
                ((V8Function) onLoadmore).call(null, parameters);
            }
        }));
    }

    @Override
    protected void onConfigureJSNode(V8Object jsNode) {
        super.onConfigureJSNode(jsNode);

        jsNode.registerJavaMethod((v8Object, v8Array) -> {
            boolean noMoreData;
            if (v8Array.getType(0) == V8Value.BOOLEAN) {
                noMoreData = v8Array.getBoolean(0);
            } else {
                noMoreData = false;
            }
            updateUI(() -> {
                if (noMoreData) {
                    getView().finishRefreshWithNoMoreData();
                } else {
                    getView().finishRefresh();
                }
            });
        }, "finishRefresh");
        jsNode.registerJavaMethod((v8Object, v8Array) -> {
            boolean noMoreData;
            if (v8Array.getType(0) == V8Value.BOOLEAN) {
                noMoreData = v8Array.getBoolean(0);
            } else {
                noMoreData = false;
            }
            updateUI(() -> {
                if (noMoreData) {
                    getView().finishLoadMoreWithNoMoreData();
                } else {
                    getView().finishLoadMore();
                }
            });
        }, "finishLoadmore");
    }

    @Override
    @CallSuper
    protected boolean onUpdateAttribute(String attrName, Object twinAttrVal) {
        switch (attrName) {
            case "refreshEnabled": {
                boolean refreshEnabled = asBool(twinAttrVal, "refreshEnabled只接受bool值");
                updateUI(() -> getView().setEnableRefresh(refreshEnabled));
                break;
            }
            case "loadmoreEnabled": {
                boolean loadmoreEnabled = asBool(twinAttrVal, "loadmoreEnabled只接受bool值");
                updateUI(() -> getView().setEnableLoadMore(loadmoreEnabled));
                break;
            }
            case "autoRefresh": {
                boolean autoRefresh = asBool(twinAttrVal, "autoRefresh只接受bool值");
                updateUI(() -> {
                    if (autoRefresh) {
                        getView().autoRefresh();
                    }
                });
                break;
            }
            case "autoLoadmore": {
                boolean autoLoadmore = asBool(twinAttrVal, "autoLoadmore只接受bool值");
                updateUI(() -> {
                    if (autoLoadmore) {
                        getView().autoLoadMore();
                    }
                });
                break;
            }
            case "onRefresh":
            case "onLoadmore":
                break;
            default:
                return super.onUpdateAttribute(attrName, twinAttrVal);
        }
        return true;
    }

}
