package com.southstate.orange.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.EditText;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
@SuppressLint("AppCompatCustomView")
public class Edit extends EditText implements IView {

    private OnSizeChangeListener mOnSizeChangeListener;

    public Edit(Context context) {
        super(context);
        setBackground(null);
        setPadding(0, 0, 0, 0);
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
