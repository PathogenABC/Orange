package com.southstate.orange.dom.impl;

import android.content.Context;

import com.eclipsesource.v8.V8Object;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.GroupNode;
import com.southstate.orange.view.Scroll;

import static com.southstate.orange.util.V8Util.asBool;

/**
 * Created by junkang on 2019/5/20.
 * Function: Orange
 */
public class ScrollNode extends GroupNode<Scroll> {

    public ScrollNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @Override
    protected Scroll createView(Context context) {
        return new Scroll(context);
    }

    @Override
    protected boolean onUpdateAttribute(String attrName, Object twinAttrVal) {
        switch (attrName) {
            case "fillViewport":
                boolean fillViewport = asBool(twinAttrVal, "fillViewport设置不正确");
                updateUI(() -> getView().setFillViewport(fillViewport));
                break;
            default:
                return super.onUpdateAttribute(attrName, twinAttrVal);
        }
        return true;
    }
}
