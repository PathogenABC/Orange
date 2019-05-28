package com.southstate.orange.dom.impl;

import android.content.Context;

import com.eclipsesource.v8.V8Object;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.Node;
import com.southstate.orange.view.View;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
public class ViewNode extends Node<View> {

    public ViewNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @Override
    protected View createView(Context context) {
        return new View(context);
    }
}
