package com.southstate.orange.dom;

import com.eclipsesource.v8.V8Object;

public class NodeComponent {
    public final V8Object mComponent;
    public final Node<?> mNode;
    private boolean isAttached = false;

    public NodeComponent(V8Object component, Node<?> dom) {
        this.mComponent = component;
        this.mNode = dom;
    }

    public void callOnViewAttached() {
        if (!isAttached) {
            isAttached = true;
            mNode.attachToWindow();
            mComponent.executeVoidFunction("__onViewAttached", null);
        }
    }

    public void callOnViewDetached() {
        if (isAttached) {
            isAttached = false;
            mNode.detachFromWindow();
            mComponent.executeVoidFunction("__onViewDetached", null);
            release();
        }
    }

    public void release() {
        mComponent.close();
        mNode.release();
        NodeFactory.freeNode(mNode);
    }
}