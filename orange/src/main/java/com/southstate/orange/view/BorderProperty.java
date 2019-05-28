package com.southstate.orange.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by junkang on 2019/5/7.
 * Function: J2V8Demo
 */
public class BorderProperty<T extends View> {

    private T mView;

    private float mBorderWidth;
    private float mBorderRadiusOrPercent;
    private boolean mRadiusIsPercent;
    private float mBorderRadiusPixels;

    private RectF mRect;
    private Path mClipPath;
    private Paint mPaint;

    public BorderProperty(T view) {
        mView = view;
        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void setBorderColor(int color) {
        mPaint.setColor(color);
        mView.postInvalidate();
    }

    public void setBorderWidth(int widthPx) {
        mBorderWidth = widthPx;
        mPaint.setStrokeWidth(widthPx);
        mClipPath = null;
        mView.postInvalidate();
    }

    public void setBorderRadius(float radiusPx, boolean isPercent) {
        mBorderRadiusOrPercent = radiusPx;
        mRadiusIsPercent = isPercent;
        mClipPath = null;
        mView.postInvalidate();
    }

    void onSizeChanged() {
        mClipPath = null;
    }

    void beforeDraw(Canvas canvas) {
        if (mBorderRadiusOrPercent > 0) {
            if (mRadiusIsPercent) {
                mBorderRadiusPixels = Math.min(mView.getWidth(), mView.getHeight()) * mBorderRadiusOrPercent / 100f;
            } else {
                mBorderRadiusPixels = mBorderRadiusOrPercent;
            }
            if (mClipPath == null) {
                mRect.set(0, 0, mView.getWidth(), mView.getHeight());
                mClipPath = new Path();
                mClipPath.addRoundRect(mRect, mBorderRadiusPixels, mBorderRadiusPixels, Path.Direction.CW);
            }
            canvas.clipPath(mClipPath);
        }
    }

    void afterDraw(Canvas canvas) {
        if (mBorderWidth > 0 && mPaint.getColor() != 0x0) {
            float hW = mBorderWidth / 2f; // border half width
            mRect.set(0 + hW, 0 + hW, mView.getWidth() - hW, mView.getHeight() - hW);
            if (mBorderRadiusOrPercent > 0) {
                canvas.drawRoundRect(mRect, mBorderRadiusPixels - hW, mBorderRadiusPixels - hW, mPaint);
            } else {
                canvas.drawRect(mRect, mPaint);
            }
        }
    }
}
