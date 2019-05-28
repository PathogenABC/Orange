package com.southstate.orange.util;

import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Value;
import com.southstate.orange.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.southstate.orange.util.DimenUtil.px;

/**
 * Created by junkang on 2019/5/20.
 * Function: Orange
 */
public final class V8Util {

    public static void close(Object... objects) {
        for (Object object : objects) {
            if (object instanceof V8Value && !((V8Value) object).isReleased()) {
                ((V8Value) object).close();
            }
        }
    }

    public static V8Array buildParam(V8 v8, Object... args) {
        V8Array param = new V8Array(v8);
        for (Object arg : args) {
            param.push(arg);
        }
        return param;
    }

    public static List<Object> toV8List(V8Array array) {
        if (array == null) {
            return null;
        }
        ArrayList<Object> objects = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            objects.add(array.get(i));
        }
        return objects;
    }

    public static Object twin(Object val) {
        if (val instanceof V8Value) {
            return ((V8Value) val).twin();
        }
        return val;
    }

    public static float asFloat(Object val, String errThrow) {
        if (val instanceof Number) {
            return ((Number) val).floatValue();
        }
        if (val instanceof String) {
            try {
                return Float.parseFloat(((String) val).trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(errThrow);
            }
        }
        throw new IllegalArgumentException(errThrow);
    }

    public static float asFloat(Object val, float fallback) {
        if (val instanceof Number) {
            return ((Number) val).floatValue();
        }
        if (val instanceof String) {
            try {
                return Float.parseFloat(((String) val).trim());
            } catch (NumberFormatException ignored) {
            }
        }
        return fallback;
    }

    public static int asColor(Object val, String errThrow) {
        if (val instanceof String) {
            try {
                return Color.parseColor(((String) val).trim());
            } catch (Exception e) {
                throw new IllegalArgumentException(errThrow);
            }
        }
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        throw new IllegalArgumentException(errThrow);
    }

    public static float asFloatOfPercent(String percentStr, String errThrow) {
        if (percentStr != null) {
            percentStr = percentStr.trim();
        }
        if (percentStr == null || percentStr.length() < 2) {
            throw new IllegalArgumentException(errThrow);
        }
        String substring = percentStr.substring(0, percentStr.length() - 1);
        try {
            return Float.parseFloat(substring);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errThrow);
        }
    }

    public static int asGravity(Object val, String errThrow) {
        if (val instanceof String) {
            int gravity = 0;
            String[] gravityArr = ((String) val).trim().split("\\|");
            for (String g : gravityArr) {
                switch (g.trim()) {
                    case "center":
                        gravity |= Gravity.CENTER;
                        break;
                    case "left":
                        gravity |= Gravity.LEFT;
                        break;
                    case "right":
                        gravity |= Gravity.RIGHT;
                        break;
                    case "top":
                        gravity |= Gravity.TOP;
                        break;
                    case "bottom":
                        gravity |= Gravity.BOTTOM;
                        break;
                    case "centerHorizontal":
                        gravity |= Gravity.CENTER_HORIZONTAL;
                        break;
                    case "centerVertical":
                        gravity |= Gravity.CENTER_VERTICAL;
                        break;
                }
            }
            return gravity;
        }
        throw new IllegalArgumentException(errThrow);
    }

    public static int asLayoutSize(Object attrVal, String errThrow) {
        if (attrVal instanceof String) {
            attrVal = ((String) attrVal).trim();
        }
        if ("wrap".equals(attrVal)) {
            return ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if ("match".equals(attrVal)) {
            return ViewGroup.LayoutParams.MATCH_PARENT;
        }
        return (int) px(asFloat(attrVal, errThrow));
    }

    public static int asOrientation(Object attrVal, String errThrow) {
        if (attrVal instanceof String) {
            attrVal = ((String) attrVal).trim();
        }
        if ("horizontal".equals(attrVal)) {
            return LinearLayout.HORIZONTAL;
        }
        if ("vertical".equals(attrVal)) {
            return LinearLayout.VERTICAL;
        }
        throw new IllegalArgumentException(errThrow);
    }

    public static int asVisibility(Object attrVal, String errThrow) {
        if (attrVal instanceof String) {
            attrVal = ((String) attrVal).trim();
        }
        if ("visible".equals(attrVal)) {
            return View.VISIBLE;
        }
        if ("invisible".equals(attrVal)) {
            return View.INVISIBLE;
        }
        if ("gone".equals(attrVal)) {
            return View.GONE;
        }
        throw new IllegalArgumentException(errThrow);
    }

    public static boolean asBool(Object attrVal, String errThrow) {
        if (attrVal instanceof String) {
            attrVal = ((String) attrVal).trim();
            if ("true".equals(attrVal)) {
                return true;
            }
            if ("false".equals(attrVal)) {
                return false;
            }
        } else if (attrVal instanceof Boolean) {
            return (boolean) attrVal;
        }
        throw new IllegalArgumentException(errThrow);
    }

    public static int asInt(Object attrVal, String errThrow) {
        if (attrVal instanceof Number) {
            return ((Number) attrVal).intValue();
        }
        if (attrVal instanceof String) {
            attrVal = ((String) attrVal).trim();
            try {
                return Integer.parseInt((String) attrVal);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(errThrow);
            }
        }
        throw new IllegalArgumentException(errThrow);
    }

    public static ImageView.ScaleType asScaleType(Object attrVal, String errThrow) {
        if (attrVal instanceof String) {
            attrVal = ((String) attrVal).trim();
            if ("center".equals(attrVal)) {
                return ImageView.ScaleType.CENTER;
            }
            if ("centerCrop".equals(attrVal)) {
                return ImageView.ScaleType.CENTER_CROP;
            }
            if ("centerInside".equals(attrVal)) {
                return ImageView.ScaleType.CENTER_INSIDE;
            }
            if ("fitCenter".equals(attrVal)) {
                return ImageView.ScaleType.FIT_CENTER;
            }
            if ("fitStart".equals(attrVal)) {
                return ImageView.ScaleType.FIT_START;
            }
            if ("fitEnd".equals(attrVal)) {
                return ImageView.ScaleType.FIT_END;
            }
            if ("fitXY".equals(attrVal)) {
                return ImageView.ScaleType.FIT_XY;
            }
            if ("matrix".equals(attrVal)) {
                return ImageView.ScaleType.MATRIX;
            }
        }
        throw new IllegalArgumentException(errThrow);
    }
}
