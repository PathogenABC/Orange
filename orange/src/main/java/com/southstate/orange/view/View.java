package com.southstate.orange.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.southstate.orange.util.ViewUtil;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
public class View extends android.view.View implements IView {

    private BorderProperty<View> mBorder;
    private OnSizeChangeListener mOnSizeChangeListener;

    public View(Context context) {
        super(context);
        mBorder = new BorderProperty<>(this);
    }

    @Override
    public BorderProperty<?> getBorder() {
        return mBorder;
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
        mBorder.onSizeChanged();
    }

    @Override
    public void draw(Canvas canvas) {
        mBorder.beforeDraw(canvas);
        super.draw(canvas);
        mBorder.afterDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewUtil.handleOnEventForTouchEffect(this, event);
        return super.onTouchEvent(event);
    }
}
