package com.southstate.orange.dom;

import com.eclipsesource.v8.V8Object;
import com.southstate.orange.OrangeView;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.impl.FrameNode;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
public class RootNode extends FrameNode {

    public RootNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
        setAttribute("layoutWidth", "match");
        setAttribute("layoutHeight", "match");
        setAttribute("backgroundColor", "#f8f8f8");
    }

    public void addToView() {
        OrangeView orangeView = getPageContext().getView();
        updateUI(() -> orangeView.addView(getView()));
        attachToWindow();
    }

    public void removeFromView() {
        OrangeView orangeView = getPageContext().getView();
        updateUI(() -> orangeView.removeView(getView()));
        detachFromWindow();
    }
}
