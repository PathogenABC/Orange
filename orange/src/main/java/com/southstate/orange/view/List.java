package com.southstate.orange.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Space;

import java.util.ArrayList;

/**
 * Created by junkang on 2019/5/20.
 * Function: Orange
 */
public class List extends ListView implements IView {

    private final BorderProperty<List> mBorder;
    private OnSizeChangeListener mOnSizeChangeListener;

    private ItemViewRequester mItemViewRequester;
    private java.util.List<View> mViewList = new ArrayList<>();
    private final Adapter mAdapter;

    public List(Context context) {
        super(context);
        mBorder = new BorderProperty<>(this);
        mAdapter = new Adapter();
        setAdapter(mAdapter);
        setDivider(null);
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
    protected void dispatchDraw(Canvas canvas) {
        mBorder.beforeDraw(canvas);
        super.dispatchDraw(canvas);
        mBorder.afterDraw(canvas);
    }

    public void setItemViewRequester(ItemViewRequester requester) {
        mItemViewRequester = requester;
    }

    public void setItemView(int position, android.view.View view) {
        mViewList.set(position, view);
        mAdapter.notifyDataSetChanged();
    }

    public void setItemViewList(java.util.List<View> viewList) {
        mViewList.clear();
        if (viewList != null) {
            mViewList.addAll(viewList);
        }
        mAdapter.notifyDataSetChanged();
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public Object getItem(int position) {
            return mViewList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {

            android.view.View view = mViewList.get(position);
            if (view == null) {
                mItemViewRequester.request(position);
                if (convertView != null) {
                    view = convertView;
                } else {
                    Space holder = new Space(parent.getContext());
                    holder.setLayoutParams(new ViewGroup.LayoutParams(parent.getWidth(), parent.getHeight()));
                    view = holder;
                }
            }

            if (position - 1 >= 0) {
                android.view.View v = mViewList.get(position - 1);
                if (v == null) {
                    mItemViewRequester.request(position - 1);
                }
            }

            if (position + 1 < mViewList.size()) {
                android.view.View v = mViewList.get(position + 1);
                if (v == null) {
                    mItemViewRequester.request(position + 1);
                }
            }

            return view;
        }
    }

    public interface ItemViewRequester {
        void request(int position);
    }

    public interface OnDetachListener {
        void onDetached();
    }
}
