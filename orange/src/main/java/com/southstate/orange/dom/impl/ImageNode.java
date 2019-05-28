package com.southstate.orange.dom.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.Node;
import com.southstate.orange.util.FileUtil;
import com.southstate.orange.util.V8Util;
import com.southstate.orange.view.Image;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
public class ImageNode extends Node<Image> {

    private static final LruCache<String, Bitmap> BASE64_BMP_CACHE = new LruCache<>(30);

    private static Bitmap getBase64Bmp(String uri) {
        Bitmap bitmap = BASE64_BMP_CACHE.get(uri);
        if (bitmap == null) {
            bitmap = FileUtil.base64Bitmap(uri.split(",")[1]);
            BASE64_BMP_CACHE.put(uri, bitmap);
        }
        return bitmap;
    }

    private String mUri;

    public ImageNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @Override
    protected Image createView(Context context) {
        return new Image(context);
    }

    @Override
    protected boolean onUpdateAttribute(String attrName, Object twinAttrValue) {
        switch (attrName) {
            case "src": {
                if (twinAttrValue instanceof String) {
                    String uri = (String) twinAttrValue;
                    if (!TextUtils.equals(mUri, uri)) {
                        if (uri.startsWith("data:image/")) {
                            mUri = uri;
                            // TODO: 2019/5/17 async
                            Bitmap bmp = getBase64Bmp(uri);
                            updateUI(() -> getView().setImageBitmap(bmp));
                        } else if (uri.startsWith("http://") || uri.startsWith("https://")) {
                            mUri = null;
                            updateUI(() -> uiSetImageUrl(uri));
                        } else {
                            mUri = null;
                        }
                    }
                } else if (twinAttrValue == null) {
                    mUri = null;
                    updateUI(() -> getView().setImageBitmap(null));
                } else {
                    throw new IllegalArgumentException("src须是String或null");
                }
                break;
            }
            case "scaleType":
                ImageView.ScaleType scaleType = V8Util.asScaleType(twinAttrValue, "scaleType设置错误");
                updateUI(() -> getView().setScaleType(scaleType));
                break;
            default:
                return super.onUpdateAttribute(attrName, twinAttrValue);
        }
        return true;
    }

    private void uiSetImageUrl(String url) {
        Glide.with(getView()).load(url).into(getView());
    }
}
