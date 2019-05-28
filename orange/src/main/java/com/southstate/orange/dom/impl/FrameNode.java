package com.southstate.orange.dom.impl;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.eclipsesource.v8.V8Object;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.GroupNode;
import com.southstate.orange.dom.Node;
import com.southstate.orange.util.ViewUtil;
import com.southstate.orange.view.Frame;

import static com.southstate.orange.util.V8Util.asGravity;

/**
 * Created by junkang on 2019/5/20.
 * Function: Orange
 */
public class FrameNode extends GroupNode<Frame> {

    public FrameNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @Override
    protected Frame createView(Context context) {
        return new Frame(context);
    }

    @CallSuper
    @Override
    protected void onUpdateChildAttribute(Node<?> child, String attrName, Object attrVal) {
        switch (attrName) {
            case "layoutGravity":
                updateUI(() -> ViewUtil.updateFrameLayoutParams(child.getView(), lp -> {
                    // 更新gravity
                    lp.gravity = asGravity(attrVal, "gravity值不正确");
                }));
                break;
            default:
                super.onUpdateChildAttribute(child, attrName, attrVal);
        }
    }

}
