package com.southstate.orange.view;

import android.content.Context;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
public class Refresh extends SmartRefreshLayout implements IView {

    private OnSizeChangeListener mOnSizeChangeListener;

    public Refresh(Context context) {
        super(context);
    }

    @Override
    public BorderProperty<?> getBorder() {
        return null;
    }

    @Override
    public void setOnSizeChangeListener(OnSizeChangeListener listener) {
        mOnSizeChangeListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mOnSizeChangeListener != null) {
            mOnSizeChangeListener.onSizeChanged(w, h);
        }
    }
}
