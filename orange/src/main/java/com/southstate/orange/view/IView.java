package com.southstate.orange.view;

/**
 * Created by junkang on 2019/5/20.
 * Function: Orange
 */
public interface IView {

    BorderProperty<?> getBorder();

    void setOnSizeChangeListener(OnSizeChangeListener listener);

    interface OnSizeChangeListener {
        void onSizeChanged(int width, int height);
    }

}
