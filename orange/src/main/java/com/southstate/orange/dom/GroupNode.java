package com.southstate.orange.dom;

import android.support.annotation.CallSuper;
import android.view.ViewGroup;

import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.southstate.orange.context.PageContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junkang on 2019/5/20.
 * Function: Orange
 */
public abstract class GroupNode<T extends ViewGroup> extends Node<T> {

    private List<Node<?>> mChildren = new ArrayList<>();

    public GroupNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @CallSuper
    @Override
    protected void onConfigureJSNode(V8Object jsNode) {
        super.onConfigureJSNode(jsNode);

        jsNode.registerJavaMethod(new AddChildFunc(), "addChild");
        jsNode.registerJavaMethod(new RemoveChildFunc(), "removeChild");
        jsNode.registerJavaMethod(new RemoveChildAtFunc(), "removeChildAt");
        jsNode.registerJavaMethod(new RemoveAllViewsFunc(), "removeAllViews");

    }

    @CallSuper
    protected void onUpdateChildAttribute(Node<?> child, String attrName, Object twinAttrVal) {
        throw new IllegalArgumentException("未知属性值[" + attrName + "]");
    }

    @Override
    public void attachToWindow() {
        super.attachToWindow();
        for (Node<?> child : mChildren) {
            child.attachToWindow();
        }
    }

    @Override
    public void detachFromWindow() {
        super.detachFromWindow();
        for (Node<?> child : mChildren) {
            child.detachFromWindow();
        }
    }

    protected void addChild(Node<?> node, int addIndex) {
        if (node.mParent != null) {
            throw new RuntimeException("node has parent");
        }
        if (addIndex < 0) {
            addIndex = mChildren.size();
        }
        mChildren.add(addIndex, node);
        node.mParent = this;
        if (this.mAttachedToWindow) {
            node.attachToWindow();
        }

        int addIndexFinal = addIndex;
        updateUI(() -> getView().addView(node.getView(), addIndexFinal));
    }

    protected void removeChild(Node<?> node) {
        if (node != null && node.mParent == this) {
            mChildren.remove(node);
            node.mParent = null;
            node.detachFromWindow();
            updateUI(() -> getView().removeView(node.getView()));
        }
    }

    protected void removeChildAt(int removeIndex) {
        removeChild(mChildren.get(removeIndex));
    }

    protected void removeAllViews() {
        ArrayList<Node<?>> copies = new ArrayList<>(mChildren);
        for (Node<?> child : copies) {
            removeChild(child);
        }
    }

    private class AddChildFunc implements JavaVoidCallback {
        @Override
        public void invoke(V8Object v8Object, V8Array v8Array) {
            if (v8Array.getType(0) != V8Value.V8_OBJECT) {
                throw new IllegalArgumentException("addChild的第一个参数必须是对象");
            }
            V8Object jsNode = v8Array.getObject(0);
            int addIndex = -1;
            if (v8Array.getType(1) == V8Value.INTEGER) {
                addIndex = v8Array.getInteger(1);
            }

            Node<?> node = NodeFactory.getNode(jsNode);
            addChild(node, addIndex);
        }

    }

    private class RemoveChildFunc implements JavaVoidCallback {
        @Override
        public void invoke(V8Object v8Object, V8Array v8Array) {
            V8Object jsNode = v8Array.getObject(0);
            Node<?> node = NodeFactory.getNode(jsNode);
            removeChild(node);
        }

    }

    private class RemoveChildAtFunc implements JavaVoidCallback {
        @Override
        public void invoke(V8Object v8Object, V8Array v8Array) {
            int removeIndex = v8Array.getInteger(0);
            removeChildAt(removeIndex);
        }

    }

    private class RemoveAllViewsFunc implements JavaVoidCallback {
        @Override
        public void invoke(V8Object v8Object, V8Array v8Array) {
            removeAllViews();
        }

    }
}
