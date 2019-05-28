package com.southstate.orange.dom.impl;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.Node;
import com.southstate.orange.dom.NodeComponent;
import com.southstate.orange.dom.NodeFactory;
import com.southstate.orange.util.V8Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by junkang on 2019/5/19.
 * Function: J2V8Demo
 */
public class ListNode extends Node<com.southstate.orange.view.List> {

    private List<Object> mData = new ArrayList<>();
    private List<NodeComponent> mNodes = new ArrayList<>();

    private List<Node<?>> mHeaderList = new ArrayList<>();
    private List<Node<?>> mFooterList = new ArrayList<>();

    public ListNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @Override
    protected com.southstate.orange.view.List createView(Context context) {
        return new com.southstate.orange.view.List(context);
    }

    @Override
    protected void onConfigureJSNode(V8Object jsNode) {
        super.onConfigureJSNode(jsNode);
        jsNode.registerJavaMethod((v8Object, v8Array) -> {
            int addIndex = -1;
            if (v8Array.getType(1) == V8Value.INTEGER) {
                addIndex = v8Array.getInteger(1);
            }
            if (v8Array.getType(0) == V8Object.V8_ARRAY) {
                V8Array array = v8Array.getArray(0);
                List<Object> data = V8Util.toV8List(array);
                addItem(data, addIndex);
                array.close();
            } else {
                throw new IllegalArgumentException("addItems只接收数组参数");
            }
        }, "addItems");
        jsNode.registerJavaMethod((v8Object, v8Array) -> {
            int index = -1;
            if (v8Array.getType(0) == V8Value.INTEGER) {
                index = v8Array.getInteger(0);
            }
            removeItemAt(index);
        }, "removeItemAt");

        jsNode.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) == V8Object.V8_OBJECT) {
                V8Object jsHeaderNode = v8Array.getObject(0);
                addHeader(jsHeaderNode);
                jsHeaderNode.close();
            } else {
                throw new IllegalArgumentException("header须是js对象");
            }
        }, "addHeader");
        jsNode.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) == V8Object.V8_OBJECT) {
                V8Object jsFooterNode = v8Array.getObject(0);
                addFooter(jsFooterNode);
                jsFooterNode.close();
            } else {
                throw new IllegalArgumentException("footer须是js对象");
            }
        }, "addFooter");
        jsNode.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) == V8Object.V8_OBJECT) {
                V8Object jsHeaderNode = v8Array.getObject(0);
                removeHeader(jsHeaderNode);
                jsHeaderNode.close();
            } else {
                throw new IllegalArgumentException("header须是js对象");
            }
        }, "removeHeader");
        jsNode.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) == V8Object.V8_OBJECT) {
                V8Object jsFooterNode = v8Array.getObject(0);
                removeFooter(jsFooterNode);
                jsFooterNode.close();
            } else {
                throw new IllegalArgumentException("footer须是js对象");
            }
        }, "removeFooter");
    }

    protected void addHeader(V8Object headerNode) {
        Node<?> node = NodeFactory.getNode(headerNode);
        if (node.getParent() != null) {
            throw new IllegalArgumentException("此组件已经被add过，无法再次被add");
        }
        if (mHeaderList.contains(node)) {
            throw new IllegalArgumentException("此组件已经在header列表中了，无法再次add");
        }
        mHeaderList.add(node);
        node.attachToWindow();
        updateUI(() -> getView().addHeaderView(node.getView()));
    }

    protected void addFooter(V8Object footerNode) {
        Node<?> node = NodeFactory.getNode(footerNode);
        if (node.getParent() != null) {
            throw new IllegalArgumentException("此组件已经被add过，无法再次被add");
        }
        if (mFooterList.contains(node)) {
            throw new IllegalArgumentException("此组件已经在footer列表中了，无法再次add");
        }
        mFooterList.add(node);
        node.attachToWindow();
        updateUI(() -> getView().addFooterView(node.getView()));
    }

    private void removeHeader(V8Object headerNode) {
        Node<?> node = NodeFactory.getNode(headerNode);
        if (node.getParent() != null) {
            throw new IllegalArgumentException("此组件被添加在别的视图上");
        }
        if (mHeaderList.remove(node)) {
            node.detachFromWindow();
            updateUI(() -> getView().removeHeaderView(node.getView()));
        }
    }

    private void removeFooter(V8Object footerNode) {
        Node<?> node = NodeFactory.getNode(footerNode);
        if (node.getParent() != null) {
            throw new IllegalArgumentException("此组件被添加在别的视图上");
        }
        if (mFooterList.remove(node)) {
            node.detachFromWindow();
            updateUI(() -> getView().removeFooterView(node.getView()));
        }
    }

    private void reRenderAllItems(List<Object> items) {

        // call destroy function
        for (NodeComponent node : mNodes) {
            if (node != null && node.mComponent != null) {
                node.callOnViewDetached();
            }
        }

        for (Object datum : mData) {
            V8Util.close(datum);
        }
        mData.clear();
        mNodes.clear();

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                Object datum = items.get(i);
                mData.add(i, datum);
                mNodes.add(i, null);
            }
        }
        syncToList();
    }

    private NodeComponent renderItemNode(int index, Object itemData) {
        V8 v8 = getPageContext().getV8();
        V8Array param = V8Util.buildParam(v8, itemData, index);
        V8Function renderItemFunc = (V8Function) getAttribute("renderItem");
        V8Object itemComponent = (V8Object) renderItemFunc.call(null, param);
        V8Util.close(param);

        V8Object jsViewNode = (V8Object) itemComponent.get("__view");
        Node<?> node = NodeFactory.getNode(jsViewNode);
        V8Util.close(jsViewNode);

        return new NodeComponent(itemComponent, node);
    }

    protected void addItem(List<Object> data, int index) {
        if (index == -1 || index > mData.size()) {
            index = mData.size();
        }

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                Object datum = data.get(i);
                mData.add(index + i, datum);
                mNodes.add(index + i, null);
            }
        }
        syncToList();
    }

    protected void removeItemAt(int index) {
        if (index < 0 || index >= mData.size()) {
            throw new IllegalArgumentException("removeItemAt out of index bounds");
        }
        Object remove = mData.remove(index);
        if (remove != null) {
            V8Util.close(remove);
        }
        NodeComponent removed = mNodes.remove(index);
        if (removed != null) {
            removed.callOnViewDetached();
        }
        syncToList();
    }

    protected void removeAllData() {
        reRenderAllItems(new ArrayList<>());
    }

    protected void updateItem(Object newVal, int index) {
        if (index < 0 || index >= mData.size()) {
            throw new IllegalArgumentException("updateItem out of index bounds");
        }

        if (!Objects.equals(newVal, (mData.get(index)))) {
            Object set = mData.set(index, newVal);
            V8Util.close(set);
            NodeComponent prev = mNodes.set(index, null);
            if (prev != null) {
                prev.callOnViewDetached();
            }
            syncToList();
        }
    }

    protected void notifyDataSetChanged() {
        Object items = getAttribute("items");
        if (items == null || items instanceof V8Array) {
            V8Array param = (V8Array) items;
            reRenderAllItems(V8Util.toV8List(param));
        }
    }

    private void syncToList() {
        if (getAttribute("renderItem") != null) {
            List<View> viewList = new ArrayList<>(mNodes.size());
            for (int i = 0; i < mNodes.size(); i++) {
                NodeComponent domNode = mNodes.get(i);
                viewList.add(domNode != null ? domNode.mNode.getView() : null);
            }
            updateUI(() -> getView().setItemViewList(viewList));
        } else {
            updateUI(() -> getView().setItemViewList(Collections.emptyList()));
        }
    }

    @UiThread
    @CallSuper
    @Override
    protected void onConfigureView() {
        super.onConfigureView();

        getView().setItemViewRequester((position) -> updateJS(() -> {
            NodeComponent itemNode = mNodes.get(position);
            if (itemNode == null) {
                itemNode = renderItemNode(position, mData.get(position));
                mNodes.set(position, itemNode);
                itemNode.callOnViewAttached();
            }
            View view = itemNode.mNode.getView();
            updateUI(() -> getView().setItemView(position, view));
        }));
    }

    @Override
    public void detachFromWindow() {
        super.detachFromWindow();
        for (NodeComponent node : mNodes) {
            if (node != null) {
                node.callOnViewDetached();
            }
        }
        for (Node<?> node : mHeaderList) {
            node.detachFromWindow();
        }
        for (Node<?> node : mFooterList) {
            node.detachFromWindow();
        }
    }

    @Override
    protected boolean onUpdateAttribute(String attrName, Object twinAttrVal) {
        switch (attrName) {
            case "renderItem": {
                if (twinAttrVal == null) {
                    throw new IllegalArgumentException("renderItem function can't be null");
                }
                reRenderAllItems(new ArrayList<>(mData));
                break;
            }
            case "items": {
                if (twinAttrVal == null || twinAttrVal instanceof V8Array) {
                    V8Array param = (V8Array) twinAttrVal;
                    reRenderAllItems(V8Util.toV8List(param));
                } else {
                    throw new IllegalArgumentException("items必须为数组或null");
                }
                break;
            }
            default:
                return super.onUpdateAttribute(attrName, twinAttrVal);
        }
        return true;
    }

    @CallSuper
    @Override
    public void release() {
        super.release();

    }
}
