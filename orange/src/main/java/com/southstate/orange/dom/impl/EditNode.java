package com.southstate.orange.dom.impl;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.TypedValue;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.dom.Node;
import com.southstate.orange.util.V8Util;
import com.southstate.orange.view.Edit;

import java.util.Objects;

import static com.southstate.orange.util.DimenUtil.px;
import static com.southstate.orange.util.V8Util.asBool;
import static com.southstate.orange.util.V8Util.asColor;
import static com.southstate.orange.util.V8Util.asFloat;
import static com.southstate.orange.util.V8Util.asGravity;
import static com.southstate.orange.util.V8Util.asInt;

/**
 * Created by junkang on 2019/5/21.
 * Function: Orange
 */
public class EditNode extends Node<Edit> {

    public EditNode(PageContext pc, V8Object jsNode) {
        super(pc, jsNode);
    }

    @Override
    protected Edit createView(Context context) {
        return new Edit(context);
    }

    @CallSuper
    @Override
    protected void onConfigureView() {
        super.onConfigureView();
        getView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = getView().getText().toString();
                updateJS(() -> {
                    onSetAttribute("text", text);
                    Object onTextChanged = getAttribute("onTextChanged");
                    if (onTextChanged instanceof V8Function) {
                        V8Array parameters = V8Util.buildParam(getPageContext().getV8(), text);
                        ((V8Function) onTextChanged).call(null, parameters);
                        parameters.close();
                    }
                });
            }
        });
    }

    @CallSuper
    @Override
    protected boolean onUpdateAttribute(String attrName, Object twinAttrVal) {
        switch (attrName) {
            case "text": {
                updateUI(() -> getView().setText(Objects.toString(twinAttrVal)));
                break;
            }
            case "gravity": {
                updateUI(() -> getView().setGravity(asGravity(twinAttrVal, "gravity设置不正确")));
                break;
            }
            case "textColor": {
                int color = asColor(twinAttrVal, "textColor颜色值不正确");
                updateUI(() -> getView().setTextColor(color));
                break;
            }
            case "textSize": {
                float textSize = px(asFloat(twinAttrVal, "textSize须是数字值"));
                updateUI(() -> getView().setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize));
                break;
            }
            case "textStyle": {
                if (twinAttrVal instanceof String) {
                    String style = ((String) twinAttrVal).trim();
                    if ("bold".equals(style)) {
                        updateUI(() -> {
                            TextPaint paint = getView().getPaint();
                            paint.setFakeBoldText(true);
                            paint.setTextSkewX(0);
                            paint.setUnderlineText(false);
                            paint.setStrikeThruText(false);
                            getView().postInvalidate();
                        });
                    } else if ("italic".equals(style)) {
                        updateUI(() -> {
                            TextPaint paint = getView().getPaint();
                            paint.setFakeBoldText(false);
                            paint.setTextSkewX(-0.3f);
                            paint.setUnderlineText(false);
                            paint.setStrikeThruText(false);
                            getView().postInvalidate();
                        });
                    } else if ("underline".equals(style)) {
                        updateUI(() -> {
                            TextPaint paint = getView().getPaint();
                            paint.setFakeBoldText(false);
                            paint.setTextSkewX(0);
                            paint.setUnderlineText(true);
                            paint.setStrikeThruText(false);
                            getView().postInvalidate();
                        });
                    } else if ("strikeThrough".equals(style)) {
                        updateUI(() -> {
                            TextPaint paint = getView().getPaint();
                            paint.setFakeBoldText(false);
                            paint.setTextSkewX(0);
                            paint.setUnderlineText(false);
                            paint.setStrikeThruText(true);
                            getView().postInvalidate();
                        });
                    } else if ("normal".equals(style)) {
                        updateUI(() -> {
                            TextPaint paint = getView().getPaint();
                            paint.setFakeBoldText(false);
                            paint.setTextSkewX(0);
                            paint.setUnderlineText(false);
                            paint.setStrikeThruText(false);
                            getView().postInvalidate();
                        });
                    }
                } else {
                    throw new IllegalArgumentException("textStyle值不正确");
                }
                break;
            }
            case "singleLine": {
                boolean singleLine = asBool(twinAttrVal, "singleLine须是boolean值");
                updateUI(() -> getView().setSingleLine(singleLine));
                break;
            }
            case "maxLines": {
                int maxLines = asInt(twinAttrVal, "maxLines须是integer值");
                updateUI(() -> getView().setMaxLines(maxLines));
                break;
            }
            case "hint": {
                String hint = String.valueOf(twinAttrVal);
                updateUI(() -> getView().setHint(hint));
                break;
            }
            case "hintColor": {
                int color = asColor(twinAttrVal, "hintColor颜色值不正确");
                updateUI(() -> getView().setHintTextColor(color));
                break;
            }
            case "onTextChanged":
                break;
            default:
                return super.onUpdateAttribute(attrName, twinAttrVal);
        }
        return true;
    }
}
