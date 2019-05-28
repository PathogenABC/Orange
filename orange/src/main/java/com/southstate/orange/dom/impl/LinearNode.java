package com.southstate.orange.dom.impl;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.eclipsesource.v8.V8Object;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.GroupNode;
import com.southstate.orange.dom.Node;
import com.southstate.orange.util.ViewUtil;
import com.southstate.orange.view.Linear;

import static com.southstate.orange.util.V8Util.asFloat;
import static com.southstate.orange.util.V8Util.asGravity;
import static com.southstate.orange.util.V8Util.asOrientation;

/**
 * Created by junkang on 2019/5/20.
 * Function: Orange
 */
public class LinearNode extends GroupNode<Linear> {

    public LinearNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @Override
    protected Linear createView(Context context) {
        return new Linear(context);
    }

    @Override
    protected boolean onUpdateAttribute(String attrName, Object twinAttrVal) {
        switch (attrName) {
            case "orientation":
                updateUI(() -> {
                    getView().setOrientation(asOrientation(twinAttrVal, "orientation值不正确"));
                });
                break;
            case "gravity":
                updateUI(() -> getView().setGravity(asGravity(twinAttrVal, "gravity值不正确")));
                break;
            default:
                return super.onUpdateAttribute(attrName, twinAttrVal);
        }
        return true;
    }

    @CallSuper
    @Override
    protected void onUpdateChildAttribute(Node<?> child, String attrName, Object attrVal) {
        switch (attrName) {
            case "layoutGravity":
                updateUI(() -> ViewUtil.updateLinearLayoutParams(child.getView(), lp -> {
                    // 更新gravity
                    lp.gravity = asGravity(attrVal, "gravity值不正确");
                }));
                break;
            case "weight":
                updateUI(() -> ViewUtil.updateLinearLayoutParams(child.getView(), lp -> {
                    // 更新weight
                    lp.weight = asFloat(attrVal, "weight须为数字值");
                }));
                break;
            default:
                super.onUpdateChildAttribute(child, attrName, attrVal);
        }
    }

}
