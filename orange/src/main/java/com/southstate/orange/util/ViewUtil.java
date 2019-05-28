package com.southstate.orange.util;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
public final class ViewUtil {

    public static void updateLayoutParams(View view, Updater<ViewGroup.MarginLayoutParams> updater) {
        ViewGroup.MarginLayoutParams mlp;
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null && lp.getClass() == ViewGroup.LayoutParams.class) {
            mlp = new ViewGroup.MarginLayoutParams(lp);
        } else if (lp == null) {
            mlp = new ViewGroup.MarginLayoutParams(0, 0);
        } else {
            mlp = (ViewGroup.MarginLayoutParams) lp;
        }
        updater.update(mlp);
        view.setLayoutParams(mlp);
    }

    public static void updateLinearLayoutParams(View view, Updater<LinearLayout.LayoutParams> updater) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new LinearLayout.LayoutParams(0, 0);
        } else {
            lp = new LinearLayout.LayoutParams(lp);
        }
        updater.update((LinearLayout.LayoutParams) lp);
        view.setLayoutParams(lp);
    }

    public static void updateFrameLayoutParams(View view, Updater<FrameLayout.LayoutParams> updater) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(0, 0);
        } else {
            lp = new FrameLayout.LayoutParams(lp);
        }
        updater.update((FrameLayout.LayoutParams) lp);
        view.setLayoutParams(lp);
    }

    public static void updatePadding(View view, Updater<Rect> updater) {
        Rect rect = new Rect();
        rect.left = view.getPaddingLeft();
        rect.top = view.getPaddingTop();
        rect.right = view.getPaddingRight();
        rect.bottom = view.getPaddingBottom();
        updater.update(rect);
        view.setPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    public static void handleOnEventForTouchEffect(View view, MotionEvent event) {
        if (view.isClickable()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                view.setAlpha(0.5f * view.getAlpha());
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                view.setAlpha(view.getAlpha() * 2f);
            }
        }
    }

    public interface Updater<T> {
        void update(@NonNull T val);
    }
}
