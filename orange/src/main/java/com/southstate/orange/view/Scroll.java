package com.southstate.orange.view;

import android.content.Context;
import android.widget.ScrollView;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
public class Scroll extends ScrollView implements IView {

    private OnSizeChangeListener mOnSizeChangeListener;

    public Scroll(Context context) {
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
